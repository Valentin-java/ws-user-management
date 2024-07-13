package com.workers.wsusermanagement.bussines.service.confirmotp.service;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.confirmotp.interfaces.ConfirmationOtpService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationOtpServiceImpl implements ConfirmationOtpService {

    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;
    private final ResetPasswordProcessFeignClient resetPasswordProcessFeignClient;
    private static final int EXCEED_TIMES = 2;

    @Override
    public ResetPasswordResponse confirmOtpProcess(ConfirmationOtpContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExceedTimesToReset)
                .map(this::validateBlockedStatus)
                .map(this::validateActivityProfileStatus)
                .map(this::findLatestInactiveOtp)
                .map(this::compareOtp)
                .map(this::activateOtp)
                .map(resetPasswordProcessFeignClient::requestToExecuteByService)
                .map(this::createResetPasswordResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль клиента"));
    }

    private ConfirmationOtpContext validateRequest(ConfirmationOtpContext ctx) {
        validationService.validate(ctx.getOtpRequest());
        return ctx;
    }

    private ConfirmationOtpContext validateExistingCustomer(ConfirmationOtpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getOtpRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ConfirmationOtpContext validateExceedTimesToReset(ConfirmationOtpContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getResetPasswordRequest().password());
        if (otpList.size() > EXCEED_TIMES) {
            ctx.getUserProfile().setActivityStatus(ActivityStatus.BLOCKED_TO_RESET);
            userProfileRepository.save(ctx.getUserProfile());
        }
        return ctx;
    }

    private ConfirmationOtpContext validateBlockedStatus(ConfirmationOtpContext ctx) {
        if (ActivityStatus.BLOCKED_TO_RESET.equals(ctx.getUserProfile().getActivityStatus())) {
            throw new ResponseStatusException(BAD_REQUEST, "Профиль заблокирован для сброса пароля");
        }
        return ctx;
    }

    private ConfirmationOtpContext validateActivityProfileStatus(ConfirmationOtpContext ctx) {
        if (ActivityStatus.INACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Отп применяется для активного профиля");
    }

    private ConfirmationOtpContext findLatestInactiveOtp(ConfirmationOtpContext ctx) {
        OtpEntity otpEntity = otpEntityRepository.findAllByUsername(ctx.getOtpRequest().phoneNumber()).stream()
                .filter(e -> ActivityStatus.INACTIVE.equals(e.getActivityStatus()))
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль в системе не существует"));

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private ConfirmationOtpContext compareOtp(ConfirmationOtpContext ctx) {
        String actualOtp = ctx.getOtpEntity().getOtp();
        String enteredOtp = ctx.getOtpRequest().otp();
        if (actualOtp.equals(enteredOtp)) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Отп не верный");
    }

    private ConfirmationOtpContext activateOtp(ConfirmationOtpContext ctx) {
        ctx.getOtpEntity().setActivityStatus(ActivityStatus.ACTIVE);
        otpEntityRepository.save(ctx.getOtpEntity());
        return ctx;
    }

    private ResetPasswordResponse createResetPasswordResponse(ConfirmationOtpContext ctx) {
        return new ResetPasswordResponse(ctx.getOtpRequest().phoneNumber(), "OK");
    }
}

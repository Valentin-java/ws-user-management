package com.workers.wsusermanagement.bussines.service.confirmotp.service;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.confirmotp.interfaces.ConfirmationOtpService;
import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationOtpServiceImpl implements ConfirmationOtpService {

    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;
    private final ResetPasswordProcessFeignClient resetPasswordProcessFeignClient;
    private static final int EXCEED_TIMES = 3;
    private static final int OTP_TTL_MINS = 10;

    @Override
    public ResetPasswordResponse doProcess(ConfirmationOtpContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExceedTimesToReset)
                .map(this::validateBlockedStatus)
                .map(this::findLatestInactiveOtp)
                .map(this::compareOtp)
                .map(this::activateOtp)
                .map(this::deactivateUserProfile)
                .map(resetPasswordProcessFeignClient::requestToExecuteByService)
                .map(this::createResetPasswordResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ConfirmationOtpContext validateRequest(ConfirmationOtpContext ctx) {
        validationService.validate(ctx.getRequest());
        return ctx;
    }

    private ConfirmationOtpContext validateExistingCustomer(ConfirmationOtpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ConfirmationOtpContext validateExceedTimesToReset(ConfirmationOtpContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getRequest().password());
        LocalDate today = LocalDate.now();

        long otpCountToday = otpList.stream()
                .filter(otp -> otp.getCreatedAt().toLocalDate().equals(today))
                .count();

        if (otpCountToday > EXCEED_TIMES) {
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

    private ConfirmationOtpContext findLatestInactiveOtp(ConfirmationOtpContext ctx) {
        OtpEntity otpEntity = otpEntityRepository.findAllByUsername(ctx.getRequest().phoneNumber()).stream()
                .filter(e -> ActivityStatus.INACTIVE.equals(e.getActivityStatus()))
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль в системе не существует"));

        if (otpEntity.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(OTP_TTL_MINS))) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private ConfirmationOtpContext compareOtp(ConfirmationOtpContext ctx) {
        String actualOtp = ctx.getOtpEntity().getOtp();
        String enteredOtp = ctx.getRequest().otp();
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

    private ConfirmationOtpContext deactivateUserProfile(ConfirmationOtpContext ctx) {
        ctx.getUserProfile().setActivityStatus(ActivityStatus.INACTIVE);
        userProfileRepository.save(ctx.getUserProfile());
        return ctx;
    }

    private ResetPasswordResponse createResetPasswordResponse(ConfirmationOtpContext ctx) {
        return new ResetPasswordResponse(ctx.getRequest().phoneNumber(), "OK");
    }
}

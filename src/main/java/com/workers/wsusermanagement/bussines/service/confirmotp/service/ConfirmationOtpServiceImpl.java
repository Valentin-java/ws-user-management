package com.workers.wsusermanagement.bussines.service.confirmotp.service;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.confirmotp.interfaces.ConfirmationOtpService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationOtpServiceImpl implements ConfirmationOtpService {

    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;

    @Override
    public ResetPasswordResponse confirmOtpProcess(ConfirmationOtpContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExistingOtp)
                .map(this::validateActivityStatusOtp)
                .map(this::activateOtp)
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

    private ConfirmationOtpContext validateExistingOtp(ConfirmationOtpContext ctx) {
        var otpEntity = otpEntityRepository.findByUsername(ctx.getOtpRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль в системе не существует"));
        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private ConfirmationOtpContext validateActivityStatusOtp(ConfirmationOtpContext ctx) {
        if (!ActivityStatus.ACTIVE.equals(ctx.getOtpEntity().getActivityStatus())) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль уже использован");
        }
        return ctx;
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

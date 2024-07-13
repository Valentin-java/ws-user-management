package com.workers.wsusermanagement.bussines.service.reset.service;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.reset.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.WsNotificationServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final OtpEntityMapper otpEntityMapper;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;
    private final WsNotificationServiceFeignClient wsNotificationServiceFeignClient;
    private static final int OTP_LENGTH = 4;
    private static final int EXCEED_TIMES = 2;

    @Override
    public ResetPasswordResponse resetPasswordProcess(ResetPasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExceedTimesToReset)
                .map(this::validateBlockedStatus)
                .map(this::deactivateUserProfile)
                .map(this::deactivateOtherOtp)
                .map(this::generateNewOtp)
                .map(this::saveOtpByUserProfile)
                .map(wsNotificationServiceFeignClient::requestToExecuteByService)
                .map(this::createResetPasswordResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль клиента"));
    }

    private ResetPasswordContext validateRequest(ResetPasswordContext ctx) {
        validationService.validate(ctx.getResetPasswordRequest());
        return ctx;
    }

    private ResetPasswordContext validateExistingCustomer(ResetPasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getResetPasswordRequest().password())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ResetPasswordContext validateExceedTimesToReset(ResetPasswordContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getResetPasswordRequest().password());
        if (otpList.size() > EXCEED_TIMES) {
            ctx.getUserProfile().setActivityStatus(ActivityStatus.BLOCKED_TO_RESET);
            userProfileRepository.save(ctx.getUserProfile());
        }
        return ctx;
    }

    private ResetPasswordContext validateBlockedStatus(ResetPasswordContext ctx) {
        if (ActivityStatus.BLOCKED_TO_RESET.equals(ctx.getUserProfile().getActivityStatus())) {
            throw new ResponseStatusException(BAD_REQUEST, "Профиль заблокирован для сброса пароля");
        }
        return ctx;
    }

    private ResetPasswordContext deactivateUserProfile(ResetPasswordContext ctx) {
        ctx.getUserProfile().setActivityStatus(ctx.getResetPasswordRequest().activityStatus());
        userProfileRepository.save(ctx.getUserProfile());
        return ctx;
    }

    private ResetPasswordContext deactivateOtherOtp(ResetPasswordContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getResetPasswordRequest().password());

        otpList.stream()
                .filter(e -> ActivityStatus.INACTIVE.equals(e.getActivityStatus()))
                .forEach(e -> e.setActivityStatus(ActivityStatus.ACTIVE));

        otpEntityRepository.saveAll(otpList);
        return ctx;
    }

    private ResetPasswordContext generateNewOtp(ResetPasswordContext ctx) {
        var otp = new SecureRandom().nextInt((int) Math.pow(10, OTP_LENGTH));
        ctx.setOtp(String.format("%04d", otp));
        return ctx;
    }

    private ResetPasswordContext saveOtpByUserProfile(ResetPasswordContext ctx) {
        var otpEntity = otpEntityMapper.toResetOtpEntity(ctx);
        otpEntityRepository.save(otpEntity);
        return ctx;
    }

    private ResetPasswordResponse createResetPasswordResponse(ResetPasswordContext ctx) {
        return new ResetPasswordResponse(ctx.getResetPasswordRequest().phoneNumber(), "OK");
    }
}

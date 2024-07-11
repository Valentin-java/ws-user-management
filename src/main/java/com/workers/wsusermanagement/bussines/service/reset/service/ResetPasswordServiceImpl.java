package com.workers.wsusermanagement.bussines.service.reset.service;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.reset.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.WsNotificationServiceFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.reset.interfaces.ResetPasswordProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
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
    private final ResetPasswordProcessFeignClient resetPasswordProcessFeignClient;
    private final WsNotificationServiceFeignClient wsNotificationServiceFeignClient;
    private static final int OTP_LENGTH = 4;

    @Override
    public ResetPasswordResponse resetPasswordProcess(ResetPasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateUniqueCustomer)
                .map(this::deactivateUserProfile)
                .map(resetPasswordProcessFeignClient::requestToExecuteByService)
                .map(this::generateNewOtp)
                .map(this::saveOtpByUserProfile)
                .map(this::saveOtpByUserProfile)
                .map(wsNotificationServiceFeignClient::requestToExecuteByService)
                .map(this::createResetPasswordResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Не удалось сбросить пароль клиента"));
    }

    private ResetPasswordContext validateRequest(ResetPasswordContext ctx) {
        validationService.validate(ctx.getResetPasswordRequest());
        return ctx;
    }

    private ResetPasswordContext validateUniqueCustomer(ResetPasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getResetPasswordRequest().password())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ResetPasswordContext deactivateUserProfile(ResetPasswordContext ctx) {
        ctx.getUserProfile().setActivityStatus(ctx.getResetPasswordRequest().activityStatus());
        userProfileRepository.save(ctx.getUserProfile());
        return ctx;
    }

    private ResetPasswordContext generateNewOtp(ResetPasswordContext ctx) {
        var otp = new SecureRandom().nextInt((int) Math.pow(10, OTP_LENGTH));
        ctx.setOtp(String.format("%04d", otp));
        return ctx;
    }

    private ResetPasswordContext saveOtpByUserProfile(ResetPasswordContext ctx) {
        var otpEntity = otpEntityMapper.toEntity(ctx);
        otpEntityRepository.save(otpEntity);
        return ctx;
    }

    private ResetPasswordResponse createResetPasswordResponse(ResetPasswordContext ctx) {
        return new ResetPasswordResponse(ctx.getResetPasswordRequest().phoneNumber(), "OK");
    }
}

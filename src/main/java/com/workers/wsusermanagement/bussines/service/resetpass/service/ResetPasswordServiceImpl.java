package com.workers.wsusermanagement.bussines.service.resetpass.service;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.resetpass.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SenOtpNotificationFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final OtpEntityMapper otpEntityMapper;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;
    private final SenOtpNotificationFeignClient senOtpNotificationFeignClient;
    private static final int OTP_LENGTH = 4;
    private static final int EXCEED_TIMES = 3;

    @Override
    public ResetPasswordResponse doProcess(ResetPasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExceedTimesToReset)
                .map(this::validateBlockedStatus)
                .map(this::deactivateOtherOtp)
                .map(this::generateNewOtp)
                .map(this::saveOtpByUserProfile)
                .map(senOtpNotificationFeignClient::requestToExecuteByService)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ResetPasswordContext validateRequest(ResetPasswordContext ctx) {
        validationService.validate(ctx.getRequest());
        return ctx;
    }

    private ResetPasswordContext validateExistingCustomer(ResetPasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().password())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ResetPasswordContext validateExceedTimesToReset(ResetPasswordContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getRequest().password());
        LocalDate today = LocalDate.now();

        // Фильтруем список OTP, чтобы оставить только те, которые были созданы сегодня
        long otpCountToday = otpList.stream()
                .filter(otp -> otp.getCreatedAt().toLocalDate().equals(today))
                .count();

        if (otpCountToday > EXCEED_TIMES) {
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

    private ResetPasswordContext deactivateOtherOtp(ResetPasswordContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getRequest().password());

        otpList.forEach(e -> e.setActivityStatus(ActivityStatus.NO_ACTUAL));

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

    private ResetPasswordResponse createResponse(ResetPasswordContext ctx) {
        return new ResetPasswordResponse(ctx.getRequest().phoneNumber(), "OK");
    }
}

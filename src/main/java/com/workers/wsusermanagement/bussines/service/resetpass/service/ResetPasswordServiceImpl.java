package com.workers.wsusermanagement.bussines.service.resetpass.service;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.resetpass.interfaces.ResetPasswordService;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.mapper.OtpEntityMapper;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SendOtpAfterResetPasswordNotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Описывает сброс активного статуса профиля, зачистку старых отп, создание и отправку нового.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final SendOtpAfterResetPasswordNotificationClient sendOtpNotificationClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final OtpEntityMapper otpEntityMapper;

    @Override
    public ResetPasswordResponse doProcess(ResetPasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::validateExistingCustomer)
                .map(this::deactivateOtherOtp)
                .map(this::createOtpEntity)
                .map(sendOtpNotificationClient::requestToExecuteByService)
                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ResetPasswordContext validateExistingCustomer(ResetPasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователя не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ResetPasswordContext deactivateOtherOtp(ResetPasswordContext ctx) {
        var otpEntityList = otpEntityRepository.findAllByUsername(ctx.getRequest().phoneNumber());
        otpEntityList.forEach(otp -> otp.setStatusOtp(StatusOtp.DECLINED));

        otpEntityRepository.saveAll(otpEntityList);
        return ctx;
    }

    private ResetPasswordContext createOtpEntity(ResetPasswordContext ctx) {
        var otpEntity = otpEntityMapper.toResetOtpEntity(ctx);
        ctx.setOtpEntity(otpEntity);
        otpEntityRepository.save(otpEntity);
        return ctx;
    }

    private ResetPasswordContext updateVisitDate(ResetPasswordContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private ResetPasswordResponse createResponse(ResetPasswordContext ctx) {
        return new ResetPasswordResponse(ctx.getOtpEntity().getUuid());
    }
}

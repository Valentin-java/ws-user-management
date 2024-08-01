package com.workers.wsusermanagement.bussines.service.setpass.service;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.setpass.interfaces.ChangePasswordService;
import com.workers.wsusermanagement.bussines.service.setpass.mapper.ChangeContextMapper;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.changepass.interfaces.ChangePasswordAfterOtpConfirmClient;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginByOtpProcessClient;
import com.workers.wsusermanagement.rest.outbound.process.notification.interfaces.SendWarnNotificationFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final ChangePasswordAfterOtpConfirmClient changePasswordAfterOtpConfirmClient;
    private final SendWarnNotificationFeignClient sendWarnNotificationFeignClient;
    private final CustomerLoginByOtpProcessClient customerLoginByOtpProcessClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final ChangeContextMapper changeContextMapper;
    private static final Integer TTL_OTP_MINS = 5;

    @Override
    public SignInResponse doProcess(ChangePasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::findOtpByUuid)
                .map(this::validateDeclineStatusOtp)
                .map(this::validateTTL)
                .map(this::findUserProfile)
                .map(this::validateApprovedStatusOtp)

                .map(changePasswordAfterOtpConfirmClient::requestToExecuteByService)
                .map(sendWarnNotificationFeignClient::requestToExecuteByService)
                .map(changeContextMapper::toVerifiedSignIn)
                .map(customerLoginByOtpProcessClient::requestToExecuteByService)

                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ChangePasswordContext findOtpByUuid(ChangePasswordContext ctx) {
        var otpEntity = otpEntityRepository.findAllByUuid(ctx.getRequest().uuid())
                .stream()
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Для вашего запроса мы не нашли одноразовый пароль"));

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private ChangePasswordContext validateDeclineStatusOtp(ChangePasswordContext ctx) {
        if (StatusOtp.DECLINED.equals(ctx.getOtpEntity().getStatusOtp())) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль отклонен");
        }
        return ctx;
    }

    private ChangePasswordContext validateTTL(ChangePasswordContext ctx) {
        LocalDateTime createdAt = ctx.getOtpEntity().getCreatedAt();
        LocalDateTime expiresAt = createdAt.plusMinutes(TTL_OTP_MINS);

        if (StatusOtp.EXPIRED.equals(ctx.getOtpEntity().getStatusOtp())
                || LocalDateTime.now().isAfter(expiresAt)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.EXPIRED);
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }
        return ctx;
    }

    private ChangePasswordContext findUserProfile(ChangePasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getOtpEntity().getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь для данного одноразового пароля не найден"));

        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ChangePasswordContext validateApprovedStatusOtp(ChangePasswordContext ctx) {
        if (StatusOtp.APPROVED.equals(ctx.getOtpEntity().getStatusOtp())) {
            ctx.setNotificationMsg("Вы пытаетесь сменить свой пароль в аккаунт");
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль не подтвержден");
    }

    private SignInResponse createResponse(SignInByOtpContext ctx) {
        return ctx.getSignInResponse();
    }
}

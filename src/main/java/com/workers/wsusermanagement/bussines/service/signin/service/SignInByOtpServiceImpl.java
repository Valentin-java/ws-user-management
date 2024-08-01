package com.workers.wsusermanagement.bussines.service.signin.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByOtpService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginByOtpProcessClient;
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
public class SignInByOtpServiceImpl implements SignInByOtpService {

    private final CustomerLoginByOtpProcessClient customerLoginByOtpProcessClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private static final Integer TTL_OTP_MINS = 3;

    @Override
    public SignInResponse doProcess(SignInByOtpContext ctx) {
        return Optional.of(ctx)
                .map(this::findOtpByUuid)
                .map(this::validateDeclineStatusOtp)
                .map(this::validateTTL)
                .map(this::findUserProfile)
                .map(this::compareOtp)
                .map(this::activateStatusUserProfile)

                .map(customerLoginByOtpProcessClient::requestToExecuteByService)

                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private SignInByOtpContext findOtpByUuid(SignInByOtpContext ctx) {
        var otpEntity = otpEntityRepository.findAllByUuid(ctx.getRequest().uuid())
                .stream()
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Для вашего запроса мы не нашли одноразовый пароль"));

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private SignInByOtpContext validateDeclineStatusOtp(SignInByOtpContext ctx) {
        if (StatusOtp.DECLINED.equals(ctx.getOtpEntity().getStatusOtp())) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль отклонен");
        }
        return ctx;
    }

    private SignInByOtpContext validateTTL(SignInByOtpContext ctx) {
        LocalDateTime createdAt = ctx.getOtpEntity().getCreatedAt();
        LocalDateTime expiresAt = createdAt.plusMinutes(TTL_OTP_MINS);

        if (StatusOtp.EXPIRED.equals(ctx.getOtpEntity().getStatusOtp())
                || LocalDateTime.now().isAfter(expiresAt)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.EXPIRED);
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }
        return ctx;
    }

    private SignInByOtpContext findUserProfile(SignInByOtpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getOtpEntity().getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь для данного одноразового пароля не найден"));

        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private SignInByOtpContext compareOtp(SignInByOtpContext ctx) {
        String actualOtp = ctx.getOtpEntity().getOtp();
        String enteredOtp = ctx.getRequest().otp();

        if (actualOtp.equals(enteredOtp)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.APPROVED);
            return ctx;
        }

        throw new ResponseStatusException(BAD_REQUEST, "Отп не верный");
    }

    private SignInByOtpContext activateStatusUserProfile(SignInByOtpContext ctx) {
        if (ActivityStatus.INACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            ctx.getUserProfile().setActivityStatus(ActivityStatus.ACTIVE);
            return ctx;
        }
        return ctx;
    }

    private SignInByOtpContext updateVisitDate(SignInByOtpContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private SignInResponse createResponse(SignInByOtpContext ctx) {
        return ctx.getSignInResponse();
    }
}

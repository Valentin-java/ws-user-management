package com.workers.wsusermanagement.bussines.service.signup.service;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.VerifySignUpService;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.activation.interfaces.UserActivationProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.assignrole.interfaces.UserAssignRoleProcessFeignClient;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginAfterRegistryClient;
import com.workers.wsusermanagement.rest.outbound.process.registry.interfaces.UserRegistryProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Описывает вторую часть регистрации пользователей, после ввода отп.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerifySignUpServiceImpl implements VerifySignUpService {

    private final UserRegistryProcessFeignClient userRegistryProcessFeignClient;
    private final UserAssignRoleProcessFeignClient userAssignRoleProcessFeignClient;
    private final UserActivationProcessFeignClient userActivationProcessFeignClient;
    private final CustomerLoginAfterRegistryClient customerLoginAfterRegistryClient;
    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private static final Integer TTL_OTP_MINS = 3;

    @Override
    public SignInResponse doProcess(VerifySignUpContext ctx) {
        return Optional.of(ctx)
                .map(this::findOtpByUuid)
                .map(this::validateDeclineStatusOtp)
                .map(this::validateTTL)
                .map(this::findUserProfile)
                .map(this::validateStatusUserProfile)
                .map(this::compareOtp)

                .map(userRegistryProcessFeignClient::requestToExecuteByService)
                .map(userAssignRoleProcessFeignClient::requestToExecuteByService)
                .map(userActivationProcessFeignClient::requestToExecuteByService)
                .map(customerLoginAfterRegistryClient::requestToExecuteByService)

                .map(this::activationCustomerProfile)
                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private VerifySignUpContext findOtpByUuid(VerifySignUpContext ctx) {
        var otpEntity = otpEntityRepository.findAllByUuid(ctx.getRequest().uuid())
                .stream()
                .max(Comparator.comparing(OtpEntity::getCreatedAt))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Для вашего запроса мы не нашли одноразовый пароль"));

        ctx.setOtpEntity(otpEntity);
        return ctx;
    }

    private VerifySignUpContext validateDeclineStatusOtp(VerifySignUpContext ctx) {
        if (StatusOtp.DECLINED.equals(ctx.getOtpEntity().getStatusOtp())) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль отклонен");
        }
        return ctx;
    }

    private VerifySignUpContext validateTTL(VerifySignUpContext ctx) {
        LocalDateTime createdAt = ctx.getOtpEntity().getCreatedAt();
        LocalDateTime expiresAt = createdAt.plusMinutes(TTL_OTP_MINS);

        if (StatusOtp.EXPIRED.equals(ctx.getOtpEntity().getStatusOtp())
                || LocalDateTime.now().isAfter(expiresAt)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.EXPIRED);
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }
        return ctx;
    }

    private VerifySignUpContext findUserProfile(VerifySignUpContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getOtpEntity().getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь для данного одноразового пароля не найден"));

        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private VerifySignUpContext validateStatusUserProfile(VerifySignUpContext ctx) {
        if (ActivityStatus.INACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Данный пользователь уже активен");
    }

    private VerifySignUpContext compareOtp(VerifySignUpContext ctx) {
        String actualOtp = ctx.getOtpEntity().getOtp();
        String enteredOtp = ctx.getRequest().otp();

        if (actualOtp.equals(enteredOtp)) {
            ctx.getOtpEntity().setStatusOtp(StatusOtp.APPROVED);
            return ctx;
        }

        throw new ResponseStatusException(BAD_REQUEST, "Отп не верный");
    }

    private VerifySignUpContext activationCustomerProfile(VerifySignUpContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setActivityStatus(ActivityStatus.ACTIVE);
        userProfileRepository.save(userProfile);

        return ctx;
    }

    private VerifySignUpContext updateVisitDate(VerifySignUpContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private SignInResponse createResponse(VerifySignUpContext ctx) {
        return ctx.getSignInResponse();
    }
}

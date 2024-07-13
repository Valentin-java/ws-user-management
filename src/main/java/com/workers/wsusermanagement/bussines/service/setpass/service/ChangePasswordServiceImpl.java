package com.workers.wsusermanagement.bussines.service.setpass.service;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.setpass.interfaces.ChangePasswordService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.repository.OtpEntityRepository;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final UserProfileRepository userProfileRepository;
    private final OtpEntityRepository otpEntityRepository;
    private final SignUpValidationService validationService;
    private static final int EXCEED_TIMES = 2;

    @Override
    public SignInResponse doProcess(ChangePasswordContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateExceedTimesToReset)
                .map(this::validateBlockedStatus)
                .map(this::validateActivityProfileStatus)
                .map(this::findActiveOtp)




                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private ChangePasswordContext validateRequest(ChangePasswordContext ctx) {
        validationService.validate(ctx.getRequest());
        return ctx;
    }

    private ChangePasswordContext validateExistingCustomer(ChangePasswordContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не существует"));
        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private ChangePasswordContext validateExceedTimesToReset(ChangePasswordContext ctx) {
        List<OtpEntity> otpList = otpEntityRepository.findAllByUsername(ctx.getRequest().password());
        if (otpList.size() > EXCEED_TIMES) {
            ctx.getUserProfile().setActivityStatus(ActivityStatus.BLOCKED_TO_RESET);
            userProfileRepository.save(ctx.getUserProfile());
        }
        return ctx;
    }
    private ChangePasswordContext validateBlockedStatus(ChangePasswordContext ctx) {
        if (ActivityStatus.BLOCKED_TO_RESET.equals(ctx.getUserProfile().getActivityStatus())) {
            throw new ResponseStatusException(BAD_REQUEST, "Профиль заблокирован для сброса пароля");
        }
        return ctx;
    }

    private ChangePasswordContext validateActivityProfileStatus(ChangePasswordContext ctx) {
        if (ActivityStatus.INACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            return ctx;
        }
        throw new ResponseStatusException(BAD_REQUEST, "Отп применяется для активного профиля");
    }

    private ChangePasswordContext findActiveOtp(ChangePasswordContext ctx) {
        var otpActiveList = otpEntityRepository.findAllByUsername(ctx.getRequest().phoneNumber()).stream()
                .filter(e -> ActivityStatus.ACTIVE.equals(e.getActivityStatus()))
                .sorted(Comparator.comparing(OtpEntity::getCreatedAt).reversed())
                .toList();

        if (otpActiveList.size() != 1) {
            throw new ResponseStatusException(BAD_REQUEST, "На момент смены пароля должен быть активным только один одноразовый пароль");
        }

        if (otpActiveList.get(0).getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new ResponseStatusException(BAD_REQUEST, "Одноразовый пароль устарел");
        }

        return ctx;
    }

    //  изменим пароль и установим в ws-auth
    // сделаем логин, отдадим токен





    private SignInResponse createResponse(ChangePasswordContext ctx) {
        return ctx.getSignInResponse();
    }
}

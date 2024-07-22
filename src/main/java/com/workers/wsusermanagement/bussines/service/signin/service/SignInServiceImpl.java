package com.workers.wsusermanagement.bussines.service.signin.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.validation.signup.SignUpValidationService;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginProcessFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final CustomerLoginProcessFeignClient customerLoginProcessFeignClient;
    private final SignUpValidationService validationService;
    private final UserProfileRepository userProfileRepository;

    @Override
    public SignInResponse doProcess(SignInContext ctx) {
        return Optional.of(ctx)
                .map(this::validateRequest)
                .map(this::validateExistingCustomer)
                .map(this::validateBlockedStatus)
                .map(this::validateActiveStatus)
                .map(customerLoginProcessFeignClient::requestToExecuteByService)
                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private SignInContext validateRequest(SignInContext ctx) {
        validationService.validate(ctx.getRequest());
        return ctx;
    }

    private SignInContext validateExistingCustomer(SignInContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователя в системе не существует"));

        return ctx.setUserProfile(userProfile);
    }

    private SignInContext validateBlockedStatus(SignInContext ctx) {
        if (!ActivityStatus.BLOCKED_TO_RESET.equals(ctx.getUserProfile().getActivityStatus())) {
            return ctx;
        }

        throw new ResponseStatusException(BAD_REQUEST, "Пользователь заблокирован по попыткам сброса пароля");
    }

    private SignInContext validateActiveStatus(SignInContext ctx) {
        if (ActivityStatus.ACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            return ctx;
        }

        throw new ResponseStatusException(BAD_REQUEST, "Пользователь в системе не активен");
    }

    private SignInContext updateVisitDate(SignInContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private SignInResponse createResponse(SignInContext ctx) {
        return ctx.getSignInResponse();
    }
}

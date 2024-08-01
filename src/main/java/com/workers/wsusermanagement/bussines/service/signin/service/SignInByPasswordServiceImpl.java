package com.workers.wsusermanagement.bussines.service.signin.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByPasswordService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.repository.UserProfileRepository;
import com.workers.wsusermanagement.rest.outbound.process.login.interfaces.CustomerLoginByPasswordProcessClient;
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
public class SignInByPasswordServiceImpl implements SignInByPasswordService {

    private final CustomerLoginByPasswordProcessClient customerLoginByPasswordProcessClient;
    private final UserProfileRepository userProfileRepository;

    @Override
    public SignInResponse doProcess(SignInByPassContext ctx) {
        return Optional.of(ctx)
                .map(this::findUserProfile)
                .map(this::activateStatusUserProfile)

                .map(customerLoginByPasswordProcessClient::requestToExecuteByService)

                .map(this::updateVisitDate)
                .map(this::createResponse)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    private SignInByPassContext findUserProfile(SignInByPassContext ctx) {
        var userProfile = userProfileRepository.findByUsername(ctx.getRequest().phoneNumber())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Пользователь для данного одноразового пароля не найден"));

        ctx.setUserProfile(userProfile);
        return ctx;
    }

    private SignInByPassContext activateStatusUserProfile(SignInByPassContext ctx) {
        if (ActivityStatus.INACTIVE.equals(ctx.getUserProfile().getActivityStatus())) {
            ctx.getUserProfile().setActivityStatus(ActivityStatus.ACTIVE);
            return ctx;
        }
        return ctx;
    }

    private SignInByPassContext updateVisitDate(SignInByPassContext ctx) {
        var userProfile = ctx.getUserProfile();
        userProfile.setLastVisitAt(LocalDateTime.now());
        userProfileRepository.save(userProfile);
        return ctx;
    }

    private SignInResponse createResponse(SignInByPassContext ctx) {
        return ctx.getSignInResponse();
    }
}

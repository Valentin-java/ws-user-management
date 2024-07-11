package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.workers.wsusermanagement.util.CommonConstant.UNEXPECTED_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractUserAuthenticationService {

    protected final SignUpService signUpService;
    protected final SignInService signInService;
    protected final SignInMapper signInMapper;

    public SignUpResponse signUp(UserSignUpRequest request) {
        return Optional.of(request)
                .map(this::mapToSignUpContext)
                .map(signUpService::signUpProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignInResponse signIn(UserSignInRequest request) {
        return Optional.of(request)
                .map(signInMapper::toServiceContext)
                .map(signInService::signInProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    protected abstract SignUpContext mapToSignUpContext(UserSignUpRequest request);
}

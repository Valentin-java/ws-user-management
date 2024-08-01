package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByOtpService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.VerifySignUpService;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
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
    protected final VerifySignUpService verifySignUpService;
    protected final SignInService signInService;
    protected final SignInByOtpService signInByOtpService;

    public SignUpResponse signUp(UserSignUpRequest request) {
        return Optional.of(request)
                .map(this::mapToSignUpContext)
                .map(signUpService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignInResponse verifySignUp(OtpRequest request) {
        return Optional.of(request)
                .map(this::mapToVerifySignUpContext)
                .map(verifySignUpService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignUpResponse signIn(UserSignInRequest request) {
        return Optional.of(request)
                .map(this::mapToSignInContext)
                .map(signInService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignInResponse signInByOtp(OtpRequest request) {
        return Optional.of(request)
                .map(this::mapToVerifySignInContext)
                .map(signInByOtpService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    protected abstract SignUpContext mapToSignUpContext(UserSignUpRequest request);

    protected abstract VerifySignUpContext mapToVerifySignUpContext(OtpRequest request);

    protected abstract SignInContext mapToSignInContext(UserSignInRequest request);

    protected abstract VerifySignInContext mapToVerifySignInContext(OtpRequest request);
}

package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByOtpService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByPasswordService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.VerifySignUpService;
import com.workers.wsusermanagement.bussines.service.signup.model.RegistryUserResponse;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.PasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;
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
    protected final SignInByPasswordService signInByPasswordService;

    public RegistryUserResponse signUp(RegistryUserDtoRequest request) {
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

    public LoginUserResponse signIn(LoginUserDtoRequest request) {
        return Optional.of(request)
                .map(this::mapToSignInContext)
                .map(signInService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignInResponse signInByOtp(OtpRequest request) {
        return Optional.of(request)
                .map(this::mapToSignInByOtpContext)
                .map(signInByOtpService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    public SignInResponse signInByPassword(PasswordRequest request) {
        return Optional.of(request)
                .map(this::mapToSignInByPassContext)
                .map(signInByPasswordService::doProcess)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, UNEXPECTED_ERROR_MESSAGE));
    }

    protected abstract SignUpContext mapToSignUpContext(RegistryUserDtoRequest request);

    protected abstract VerifySignUpContext mapToVerifySignUpContext(OtpRequest request);

    protected abstract SignInContext mapToSignInContext(LoginUserDtoRequest request);

    protected abstract SignInByOtpContext mapToSignInByOtpContext(OtpRequest request);

    protected abstract SignInByPassContext mapToSignInByPassContext(PasswordRequest request);
}

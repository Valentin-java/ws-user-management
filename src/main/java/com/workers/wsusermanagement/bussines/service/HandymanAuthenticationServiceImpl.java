package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.HandymanAuthenticationService;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByOtpService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.VerifySignUpService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.SignUpMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.VerifySignUpMapper;
import org.springframework.stereotype.Service;

@Service
public class HandymanAuthenticationServiceImpl
        extends AbstractUserAuthenticationService
        implements HandymanAuthenticationService {

    private final SignUpMapper signUpMapper;
    private final VerifySignUpMapper verifySignUpMapper;
    private final SignInMapper signInMapper;

    public HandymanAuthenticationServiceImpl(SignUpService signUpService,
                                             VerifySignUpService verifySignUpService,
                                             SignInService signInService,
                                             SignInByOtpService signInByOtpService,
                                             SignUpMapper signUpMapper,
                                             SignInMapper signInMapper,
                                             VerifySignUpMapper verifySignUpMapper) {
        super(signUpService, verifySignUpService, signInService, signInByOtpService);
        this.signUpMapper = signUpMapper;
        this.verifySignUpMapper = verifySignUpMapper;
        this.signInMapper = signInMapper;
    }

    @Override
    protected SignUpContext mapToSignUpContext(UserSignUpRequest request) {
        return signUpMapper.toHandymanServiceContext(request);
    }

    @Override
    protected VerifySignUpContext mapToVerifySignUpContext(OtpRequest request) {
        return verifySignUpMapper.toHandymanServiceContext(request);
    }

    @Override
    protected SignInContext mapToSignInContext(UserSignInRequest request) {
        return signInMapper.toServiceContext(request);
    }

    @Override
    protected VerifySignInContext mapToVerifySignInContext(OtpRequest request) {
        return signInMapper.toVerifyServiceContext(request);
    }
}

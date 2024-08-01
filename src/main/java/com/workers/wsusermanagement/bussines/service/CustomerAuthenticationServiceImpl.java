package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.CustomerAuthenticationService;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInByOtpService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.VerifySignUpService;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.PasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.SignUpMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.VerifySignUpMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationServiceImpl
        extends AbstractUserAuthenticationService
        implements CustomerAuthenticationService {

    private final SignUpMapper signUpMapper;
    private final VerifySignUpMapper verifySignUpMapper;
    private final SignInMapper signInMapper;

    public CustomerAuthenticationServiceImpl(SignUpService signUpService,
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
    protected SignUpContext mapToSignUpContext(RegistryUserDtoRequest request) {
        return signUpMapper.toCustomerServiceContext(request);
    }

    @Override
    protected VerifySignUpContext mapToVerifySignUpContext(OtpRequest request) {
        return verifySignUpMapper.toCustomerServiceContext(request);
    }

    @Override
    protected SignInContext mapToSignInContext(LoginUserDtoRequest request) {
        return signInMapper.toServiceContext(request);
    }

    @Override
    protected SignInByOtpContext mapToSignInByOtpContext(OtpRequest request) {
        return signInMapper.toSignInByOtpContext(request);
    }

    @Override
    protected SignInByPassContext mapToSignInByPassContext(PasswordRequest request) {
        return signInMapper.toSignInByPassContext(request);
    }
}

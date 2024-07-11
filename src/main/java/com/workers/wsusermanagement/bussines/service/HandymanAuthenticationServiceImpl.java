package com.workers.wsusermanagement.bussines.service;

import com.workers.wsusermanagement.bussines.interfaces.HandymanAuthenticationService;
import com.workers.wsusermanagement.bussines.service.signin.interfaces.SignInService;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.interfaces.SignUpService;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import com.workers.wsusermanagement.rest.inbound.mapper.SignInMapper;
import com.workers.wsusermanagement.rest.inbound.mapper.SignUpMapper;
import org.springframework.stereotype.Service;

@Service
public class HandymanAuthenticationServiceImpl extends AbstractUserAuthenticationService implements HandymanAuthenticationService {

    private final SignUpMapper signUpMapper;

    public HandymanAuthenticationServiceImpl(SignUpService signUpService,
                                             SignInService signInService,
                                             SignUpMapper signUpMapper,
                                             SignInMapper signInMapper) {
        super(signUpService, signInService, signInMapper);
        this.signUpMapper = signUpMapper;
    }

    @Override
    protected SignUpContext mapToSignUpContext(UserSignUpRequest request) {
        return signUpMapper.toHandymanServiceContext(request);
    }
}

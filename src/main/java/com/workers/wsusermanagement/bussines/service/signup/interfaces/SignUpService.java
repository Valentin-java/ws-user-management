package com.workers.wsusermanagement.bussines.service.signup.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;

public interface SignUpService {

    SignUpResponse signUpProcess(SignUpContext request);
}

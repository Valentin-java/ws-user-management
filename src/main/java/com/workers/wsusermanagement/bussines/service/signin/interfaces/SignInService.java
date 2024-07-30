package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;

public interface SignInService {

    SignUpResponse doProcess(SignInContext ctx);
}

package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;

public interface SignInService {

    SignInResponse signInProcess(SignInContext ctx);
}

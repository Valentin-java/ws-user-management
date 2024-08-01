package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;

public interface SignInService {

    LoginUserResponse doProcess(SignInContext ctx);
}

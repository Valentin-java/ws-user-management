package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;

public interface VerifySignInService {

    SignInResponse doProcess(VerifySignInContext ctx);
}

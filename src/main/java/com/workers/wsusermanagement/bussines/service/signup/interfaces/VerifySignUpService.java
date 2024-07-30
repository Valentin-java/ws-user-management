package com.workers.wsusermanagement.bussines.service.signup.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;

public interface VerifySignUpService {

    SignInResponse doProcess(VerifySignUpContext request);
}

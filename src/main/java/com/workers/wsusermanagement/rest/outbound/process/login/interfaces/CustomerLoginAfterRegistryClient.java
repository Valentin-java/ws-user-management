package com.workers.wsusermanagement.rest.outbound.process.login.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;

public interface CustomerLoginAfterRegistryClient {

    VerifySignUpContext requestToExecuteByService(VerifySignUpContext ctx);
}

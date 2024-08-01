package com.workers.wsusermanagement.rest.outbound.process.login.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;

public interface CustomerLoginByPasswordProcessClient {

    SignInByPassContext requestToExecuteByService(SignInByPassContext ctx);
}

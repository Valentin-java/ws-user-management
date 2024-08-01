package com.workers.wsusermanagement.rest.outbound.process.login.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;

public interface CustomerLoginByOtpProcessClient {

    SignInByOtpContext requestToExecuteByService(SignInByOtpContext ctx);
}

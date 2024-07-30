package com.workers.wsusermanagement.rest.outbound.process.login.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;

public interface CustomerLoginProcessFeignClient {

    VerifySignInContext requestToExecuteByService(VerifySignInContext ctx);
}

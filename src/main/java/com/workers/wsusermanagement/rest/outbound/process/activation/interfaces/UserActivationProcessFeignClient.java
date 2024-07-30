package com.workers.wsusermanagement.rest.outbound.process.activation.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;

public interface UserActivationProcessFeignClient {

    VerifySignUpContext requestToExecuteByService(VerifySignUpContext ctx);
}

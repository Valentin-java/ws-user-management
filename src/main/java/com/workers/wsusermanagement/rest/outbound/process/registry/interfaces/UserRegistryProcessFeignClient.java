package com.workers.wsusermanagement.rest.outbound.process.registry.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;

public interface UserRegistryProcessFeignClient {

    VerifySignUpContext requestToExecuteByService(VerifySignUpContext ctx);
}

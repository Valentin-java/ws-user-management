package com.workers.wsusermanagement.rest.outbound.process.registry.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;

public interface UserRegistryProcessFeignClient {

    SignUpContext requestToExecuteByService(SignUpContext ctx);
}

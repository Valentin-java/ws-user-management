package com.workers.wsusermanagement.rest.outbound.process.activation.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;

public interface UserActivationProcessFeignClient {

    SignUpContext requestToExecuteByService(SignUpContext ctx);
}

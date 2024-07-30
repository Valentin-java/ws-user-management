package com.workers.wsusermanagement.rest.outbound.process.assignrole.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;

public interface UserAssignRoleProcessFeignClient {

    VerifySignUpContext requestToExecuteByService(VerifySignUpContext ctx);
}

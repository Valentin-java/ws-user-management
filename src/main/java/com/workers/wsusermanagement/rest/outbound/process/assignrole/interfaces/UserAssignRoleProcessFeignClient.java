package com.workers.wsusermanagement.rest.outbound.process.assignrole.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;

public interface UserAssignRoleProcessFeignClient {

    SignUpContext requestToExecuteByService(SignUpContext ctx);
}

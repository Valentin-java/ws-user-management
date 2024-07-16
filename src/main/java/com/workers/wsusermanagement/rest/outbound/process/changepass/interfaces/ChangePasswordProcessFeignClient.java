package com.workers.wsusermanagement.rest.outbound.process.changepass.interfaces;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;

public interface ChangePasswordProcessFeignClient {

    ChangePasswordContext requestToExecuteByService(ChangePasswordContext ctx);
}

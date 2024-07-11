package com.workers.wsusermanagement.rest.outbound.process.reset.interfaces;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;

public interface ResetPasswordProcessFeignClient {

    ResetPasswordContext requestToResetPassword(ResetPasswordContext ctx);
}

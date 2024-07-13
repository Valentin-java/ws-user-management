package com.workers.wsusermanagement.rest.outbound.process.notification.interfaces;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;

public interface WsNotificationServiceFeignClient {

    ResetPasswordContext requestToExecuteByService(ResetPasswordContext ctx);
}

package com.workers.wsusermanagement.rest.outbound.process.notification.interfaces;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;

public interface SendWarnNotificationFeignClient {

    ChangePasswordContext requestToExecuteByService(ChangePasswordContext ctx);
}

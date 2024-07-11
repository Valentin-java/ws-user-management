package com.workers.wsusermanagement.rest.outbound.process.notification.interfaces;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;

public interface WsNotificationServiceFeignClient {

    ResetPasswordContext requestToSendOtp(ResetPasswordContext ctx);
}

package com.workers.wsusermanagement.rest.outbound.process.notification.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;

public interface SendSignInOtpNotificationClient {

    SignInContext requestToExecuteByService(SignInContext ctx);
}

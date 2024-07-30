package com.workers.wsusermanagement.rest.outbound.process.notification.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;

public interface SendSignUpOtpNotificationClient {

    SignUpContext requestToExecuteByService(SignUpContext ctx);
}

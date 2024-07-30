package com.workers.wsusermanagement.rest.outbound.process.reset.interfaces;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;

public interface ResetPasswordAfterConfirmOtpProcessClient {

    ConfirmationOtpContext requestToExecuteByService(ConfirmationOtpContext ctx);
}

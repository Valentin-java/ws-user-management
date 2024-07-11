package com.workers.wsusermanagement.bussines.service.confirmotp.interfaces;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;

public interface ConfirmationOtpService {

    ResetPasswordResponse confirmOtpProcess(ConfirmationOtpContext ctx);
}

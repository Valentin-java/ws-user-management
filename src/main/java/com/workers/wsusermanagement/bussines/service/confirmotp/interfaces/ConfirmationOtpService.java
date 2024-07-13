package com.workers.wsusermanagement.bussines.service.confirmotp.interfaces;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;

public interface ConfirmationOtpService {

    ResetPasswordResponse doProcess(ConfirmationOtpContext ctx);
}

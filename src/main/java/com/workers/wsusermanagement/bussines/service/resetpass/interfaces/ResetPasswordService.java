package com.workers.wsusermanagement.bussines.service.resetpass.interfaces;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordResponse;

public interface ResetPasswordService {

    ResetPasswordResponse doProcess(ResetPasswordContext ctx);
}

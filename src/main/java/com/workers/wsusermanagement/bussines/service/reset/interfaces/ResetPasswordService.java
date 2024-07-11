package com.workers.wsusermanagement.bussines.service.reset.interfaces;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordResponse;

public interface ResetPasswordService {

    ResetPasswordResponse resetPasswordProcess(ResetPasswordContext ctx);
}

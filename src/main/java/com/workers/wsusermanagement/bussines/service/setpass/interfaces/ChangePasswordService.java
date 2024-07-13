package com.workers.wsusermanagement.bussines.service.setpass.interfaces;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;

public interface ChangePasswordService {

    SignInResponse doProcess(ChangePasswordContext ctx);
}

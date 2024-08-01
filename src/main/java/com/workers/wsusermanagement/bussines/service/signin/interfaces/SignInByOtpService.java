package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;

public interface SignInByOtpService {

    SignInResponse doProcess(SignInByOtpContext ctx);
}

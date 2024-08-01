package com.workers.wsusermanagement.bussines.service.signin.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;

public interface SignInByPasswordService {

    SignInResponse doProcess(SignInByPassContext ctx);
}

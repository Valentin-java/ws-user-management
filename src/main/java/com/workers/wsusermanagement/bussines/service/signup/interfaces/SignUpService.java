package com.workers.wsusermanagement.bussines.service.signup.interfaces;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.bussines.service.signup.model.RegistryUserResponse;

public interface SignUpService {

    RegistryUserResponse doProcess(SignUpContext request);
}

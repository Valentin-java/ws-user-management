package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;

public interface UserAuthenticationService {

    LoginUserResponse signUp(RegistryUserDtoRequest request);

    SignInResponse signIn(LoginUserDtoRequest request);
}

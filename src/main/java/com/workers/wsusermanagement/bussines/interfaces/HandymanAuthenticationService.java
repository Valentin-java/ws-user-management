package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.model.RegistryUserResponse;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;

public interface HandymanAuthenticationService {

    RegistryUserResponse signUp(RegistryUserDtoRequest request);

    SignInResponse verifySignUp(OtpRequest request);

    LoginUserResponse signIn(LoginUserDtoRequest request);

    SignInResponse signInByOtp(OtpRequest request);
}

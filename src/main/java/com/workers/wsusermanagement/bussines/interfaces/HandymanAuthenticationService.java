package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;

public interface HandymanAuthenticationService {

    SignUpResponse signUp(UserSignUpRequest request);

    SignInResponse verifySignUp(OtpRequest request);

    SignUpResponse signIn(UserSignInRequest request);

    SignInResponse signInByOtp(OtpRequest request);
}

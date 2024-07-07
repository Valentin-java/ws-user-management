package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignInRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpResponse;

public interface CustomerService {

    SignUpResponse signUp(CustomerSignUpRequest request);

    void validateOtp(OtpRequest request);

    SignInResponse signIn(CustomerSignInRequest request);

    SignUpResponse restoreProfile(SignUpRequest request);
}

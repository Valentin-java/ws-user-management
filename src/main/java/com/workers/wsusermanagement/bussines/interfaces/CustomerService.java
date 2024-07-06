package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;

public interface CustomerService {

    SignUpResponse signUp(SignUpRequest request);

    void validateOtp(OtpRequest request);

    SignUpResponse signIn(SignUpRequest request);

    SignUpResponse restoreProfile(SignUpRequest request);
}

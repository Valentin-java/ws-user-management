package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.rest.dto.OtpRequest;
import com.workers.wsusermanagement.rest.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.dto.SignUpResponse;

public interface CustomerService {

    SignUpResponse signingUp(SignUpRequest request);

    void validateOtp(OtpRequest request);
}

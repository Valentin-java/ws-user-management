package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpResponse;

public interface CustomerService {

    SignUpResponse signingUp(SignUpRequest request);

    void validateOtp(OtpRequest request);
}

package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;

public interface SignUpValidationService {

    SignUpRequest validate(SignUpRequest request);
}

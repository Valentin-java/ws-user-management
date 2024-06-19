package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.rest.dto.SignUpRequest;

public interface SignUpValidationService {

    SignUpRequest validate(SignUpRequest request);
}

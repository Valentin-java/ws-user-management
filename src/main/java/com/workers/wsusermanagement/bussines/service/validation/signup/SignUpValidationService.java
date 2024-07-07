package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;

public interface SignUpValidationService {

    void validate(SignRequest request);
}

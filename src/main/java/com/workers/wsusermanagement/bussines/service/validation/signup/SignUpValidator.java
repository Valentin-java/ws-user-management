package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;

import java.util.List;

public interface SignUpValidator {

    Integer getOrder();

    void validate(SignRequest request, List<String> errors);
}

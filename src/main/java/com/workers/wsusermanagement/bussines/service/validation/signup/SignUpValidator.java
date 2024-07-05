package com.workers.wsusermanagement.bussines.service.validation.signup;

import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;

import java.util.List;

public interface SignUpValidator {

    Integer getOrder();

    void validate(SignUpRequest request, List<String> errors);
}

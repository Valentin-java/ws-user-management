package com.workers.wsusermanagement.bussines.service.signin.model;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;

public record SignInRequest(
        String phoneNumber,
        String password
) implements SignRequest {
}

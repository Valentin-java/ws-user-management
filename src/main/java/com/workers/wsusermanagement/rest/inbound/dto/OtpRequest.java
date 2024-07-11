package com.workers.wsusermanagement.rest.inbound.dto;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;

public record OtpRequest(
        String phoneNumber,
        String otp
) implements SignRequest {

    @Override
    public String password() {
        return "no pass";
    }
}

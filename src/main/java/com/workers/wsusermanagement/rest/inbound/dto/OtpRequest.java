package com.workers.wsusermanagement.rest.inbound.dto;

public record OtpRequest(
        String uuid,
        String otp
) {
}

package com.workers.wsusermanagement.rest.inbound.dto;

public record OtpRequest(
        String phoneNumber,
        String otp
) {}

package com.workers.wsusermanagement.rest.dto;

public record OtpRequest(
        String phoneNumber,
        String otp
) {}

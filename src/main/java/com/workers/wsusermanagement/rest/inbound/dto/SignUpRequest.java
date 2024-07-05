package com.workers.wsusermanagement.rest.inbound.dto;

public record SignUpRequest(
        String phoneNumber,
        String password
) {}

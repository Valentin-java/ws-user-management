package com.workers.wsusermanagement.rest.dto;

public record SignUpRequest(
        String phoneNumber,
        String password
) {}

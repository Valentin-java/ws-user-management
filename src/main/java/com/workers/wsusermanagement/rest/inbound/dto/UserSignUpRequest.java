package com.workers.wsusermanagement.rest.inbound.dto;

public record UserSignUpRequest(
        String phoneNumber,
        String password
) {
}

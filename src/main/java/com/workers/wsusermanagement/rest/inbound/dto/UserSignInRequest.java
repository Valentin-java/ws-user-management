package com.workers.wsusermanagement.rest.inbound.dto;

public record UserSignInRequest(
        String phoneNumber,
        String password
) {
}

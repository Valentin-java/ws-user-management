package com.workers.wsusermanagement.rest.inbound.dto;

public record CustomerSignUpRequest(
        String phoneNumber,
        String password
) {
}

package com.workers.wsusermanagement.rest.inbound.dto;

public record PasswordRequest(
        String phoneNumber,
        String password
) {
}

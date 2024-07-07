package com.workers.wsusermanagement.rest.inbound.dto;

public record CustomerSignInRequest(
        String phoneNumber,
        String password
) {
}

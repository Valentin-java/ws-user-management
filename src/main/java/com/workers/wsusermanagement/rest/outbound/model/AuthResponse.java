package com.workers.wsusermanagement.rest.outbound.model;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}

package com.workers.wsusermanagement.rest.outbound.feign.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}

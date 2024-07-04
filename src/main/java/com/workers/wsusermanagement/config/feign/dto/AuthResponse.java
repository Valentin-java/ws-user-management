package com.workers.wsusermanagement.config.feign.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}

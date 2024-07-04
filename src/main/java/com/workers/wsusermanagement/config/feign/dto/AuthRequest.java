package com.workers.wsusermanagement.config.feign.dto;

public record AuthRequest(
        String username,
        String password
) {
}

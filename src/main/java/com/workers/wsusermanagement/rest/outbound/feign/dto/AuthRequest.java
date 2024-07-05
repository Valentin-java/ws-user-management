package com.workers.wsusermanagement.rest.outbound.feign.dto;

public record AuthRequest(
        String username,
        String password
) {
}

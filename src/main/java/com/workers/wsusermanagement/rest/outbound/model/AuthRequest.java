package com.workers.wsusermanagement.rest.outbound.model;

public record AuthRequest(
        String username,
        String password
) {
}

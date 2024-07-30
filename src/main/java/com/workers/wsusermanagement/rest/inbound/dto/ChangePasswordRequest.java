package com.workers.wsusermanagement.rest.inbound.dto;

public record ChangePasswordRequest(
        String uuid,
        String password
) {
}

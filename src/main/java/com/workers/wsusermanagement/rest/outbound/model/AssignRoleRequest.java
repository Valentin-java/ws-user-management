package com.workers.wsusermanagement.rest.outbound.model;

public record AssignRoleRequest(
        String username,
        String role
) {
}

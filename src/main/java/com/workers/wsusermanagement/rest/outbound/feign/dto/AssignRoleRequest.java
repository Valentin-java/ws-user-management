package com.workers.wsusermanagement.rest.outbound.feign.dto;

public record AssignRoleRequest(
        String username,
        String role
) {
}

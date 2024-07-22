package com.workers.wsusermanagement.rest.inbound.dto;

public record UserProfileRequest(
        String phoneNumber,
        String email,
        String firstName,
        String lastName
) {
}

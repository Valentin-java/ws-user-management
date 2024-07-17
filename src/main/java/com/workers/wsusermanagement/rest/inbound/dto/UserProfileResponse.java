package com.workers.wsusermanagement.rest.inbound.dto;

public record UserProfileResponse(
        String phoneNumber,
        String email,
        String firstName,
        String lastName
) {
}

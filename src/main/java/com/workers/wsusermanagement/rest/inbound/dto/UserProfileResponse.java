package com.workers.wsusermanagement.rest.inbound.dto;

public record UserProfileResponse(
        String phoneNumber,
        String firstName,
        Boolean otp
) {
}

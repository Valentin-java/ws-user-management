package com.workers.wsusermanagement.rest.inbound.dto;

public record ResetUserPasswordRequest(
        String phoneNumber
) {
}

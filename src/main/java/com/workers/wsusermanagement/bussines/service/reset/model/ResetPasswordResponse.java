package com.workers.wsusermanagement.bussines.service.reset.model;

public record ResetPasswordResponse(
        String phoneNumber,
        String text
) {
}

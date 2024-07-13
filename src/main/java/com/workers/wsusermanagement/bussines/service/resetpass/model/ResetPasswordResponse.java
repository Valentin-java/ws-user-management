package com.workers.wsusermanagement.bussines.service.resetpass.model;

public record ResetPasswordResponse(
        String phoneNumber,
        String text
) {
}

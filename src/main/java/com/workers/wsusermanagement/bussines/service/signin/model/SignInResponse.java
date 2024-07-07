package com.workers.wsusermanagement.bussines.service.signin.model;

public record SignInResponse(
        String phoneNumber,
        String accessToken,
        String refreshToken
) {
}

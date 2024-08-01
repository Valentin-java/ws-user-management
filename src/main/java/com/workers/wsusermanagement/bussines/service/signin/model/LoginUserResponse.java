package com.workers.wsusermanagement.bussines.service.signin.model;

public record LoginUserResponse(
        String uuid,
        Boolean otp
) {
}

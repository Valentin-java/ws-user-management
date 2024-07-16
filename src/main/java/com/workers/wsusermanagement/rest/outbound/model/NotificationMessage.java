package com.workers.wsusermanagement.rest.outbound.model;

public record NotificationMessage(
        String phoneNumber,
        String message
) {
}

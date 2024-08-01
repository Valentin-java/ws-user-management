package com.workers.wsusermanagement.rest.inbound.dto;

public record RegistryUserDtoRequest(
        String phoneNumber,
        String firstName
) {
}

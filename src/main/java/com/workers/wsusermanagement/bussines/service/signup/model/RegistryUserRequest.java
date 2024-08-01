package com.workers.wsusermanagement.bussines.service.signup.model;

import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;

public record RegistryUserRequest(
        String phoneNumber,
        String firstName,
        ActivityStatus activityStatus,
        CustomerRole customerRole
) {
}

package com.workers.wsusermanagement.rest.inbound.dto;

import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;

public record SignUpRequest(
        String phoneNumber,
        String password,
        ActivityStatus activityStatus,
        CustomerRole customerRole
) {}

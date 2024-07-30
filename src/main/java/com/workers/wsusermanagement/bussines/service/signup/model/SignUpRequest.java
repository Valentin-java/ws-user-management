package com.workers.wsusermanagement.bussines.service.signup.model;

import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;

public record SignUpRequest (
        String phoneNumber,
        String firstName,
        ActivityStatus activityStatus,
        CustomerRole customerRole
) {

}

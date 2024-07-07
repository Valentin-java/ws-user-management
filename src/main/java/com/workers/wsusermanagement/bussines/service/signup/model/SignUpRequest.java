package com.workers.wsusermanagement.bussines.service.signup.model;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;

public record SignUpRequest (
        String phoneNumber,
        String password,
        ActivityStatus activityStatus,
        CustomerRole customerRole
) implements SignRequest {}

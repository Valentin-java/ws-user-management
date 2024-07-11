package com.workers.wsusermanagement.bussines.service.reset.model;

import com.workers.wsusermanagement.bussines.service.common.model.SignRequest;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;

public record ResetPasswordRequest(
        String phoneNumber,
        ActivityStatus activityStatus
) implements SignRequest {

    @Override
    public String password() {
        return "no pass";
    }
}

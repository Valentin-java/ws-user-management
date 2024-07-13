package com.workers.wsusermanagement.bussines.service.resetpass.context;

import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordRequest;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResetPasswordContext {
    private ResetPasswordRequest request;
    private UserProfile userProfile;
    private NotificationMessage notificationRequest;
    private String otp;
}

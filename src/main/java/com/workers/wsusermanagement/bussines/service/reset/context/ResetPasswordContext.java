package com.workers.wsusermanagement.bussines.service.reset.context;

import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordRequest;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResetPasswordContext {
    private ResetPasswordRequest resetPasswordRequest;
    private UserProfile userProfile;
    private NotificationMessage notificationRequest;
    private String otp;
}

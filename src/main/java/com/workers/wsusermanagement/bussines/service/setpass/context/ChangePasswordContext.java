package com.workers.wsusermanagement.bussines.service.setpass.context;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.inbound.dto.ChangePasswordRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangePasswordContext {
    private ChangePasswordRequest request;
    private AuthRequest authRequest;
    private NotificationMessage notificationRequest;
    private SignInResponse signInResponse;
    private String notificationMsg;
    private UserProfile userProfile;
    private OtpEntity otpEntity;
}

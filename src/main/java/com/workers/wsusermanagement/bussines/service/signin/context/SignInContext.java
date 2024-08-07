package com.workers.wsusermanagement.bussines.service.signin.context;

import com.workers.wsusermanagement.bussines.service.signin.model.LoginUserResponse;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInRequest;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignInContext {
    private SignInRequest request;
    private LoginUserResponse loginUserResponse;
    private NotificationMessage notificationRequest;
    private OtpEntity otpEntity;
    private UserProfile userProfile;
}

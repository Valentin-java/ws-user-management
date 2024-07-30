package com.workers.wsusermanagement.bussines.service.signup.context;

import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignUpContext {
    private SignUpRequest request;
    private NotificationMessage notificationRequest;
    private OtpEntity otpEntity;
}

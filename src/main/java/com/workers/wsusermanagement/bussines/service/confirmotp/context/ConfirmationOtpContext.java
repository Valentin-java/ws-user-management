package com.workers.wsusermanagement.bussines.service.confirmotp.context;

import com.workers.wsusermanagement.bussines.service.reset.model.ResetPasswordRequest;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import lombok.Data;

@Data
public class ConfirmationOtpContext {
    private ResetPasswordRequest resetPasswordRequest;
    private OtpRequest otpRequest;
    private AuthRequest authRequest;
    private UserProfile userProfile;
    private OtpEntity otpEntity;
}

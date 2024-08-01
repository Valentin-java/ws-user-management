package com.workers.wsusermanagement.bussines.service.signin.context;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignInByOtpContext {
    private OtpRequest request;
    private AuthRequest authRequest;
    private SignInResponse signInResponse;
    private OtpEntity otpEntity;
    private UserProfile userProfile;
}

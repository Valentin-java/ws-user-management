package com.workers.wsusermanagement.bussines.service.signin.context;

import com.workers.wsusermanagement.bussines.service.signin.model.SignInRequest;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInResponse;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthRequest;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignInContext {
    private SignInRequest signInRequest;
    private AuthRequest authRequest;
    private SignInResponse signInResponse;
    private UserProfile userProfile;
}

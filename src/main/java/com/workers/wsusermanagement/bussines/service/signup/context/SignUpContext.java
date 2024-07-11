package com.workers.wsusermanagement.bussines.service.signup.context;

import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.rest.outbound.model.AssignRoleRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignUpContext {
    private SignUpRequest signUpRequest;
    private AuthRequest authRequest;
    private AssignRoleRequest assignRoleRequest;
}

package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.bussines.service.resetpass.model.ResetPasswordRequest;
import com.workers.wsusermanagement.bussines.service.signin.model.SignInRequest;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.bussines.service.signup.model.SignUpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.outbound.model.AssignRoleRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthRequestMapper {

    @Mapping(target = "username", source = "phoneNumber")
    AuthRequest toAuthRequest(SignUpRequest request);

    @Mapping(target = "username", source = "phoneNumber")
    AuthRequest toAuthRequest(SignInRequest request);

    @Mapping(target = "username", source = "phoneNumber")
    @Mapping(target = "password", ignore = true)
    AuthRequest toAuthRequest(OtpRequest request);

    @Mapping(target = "username", source = "phoneNumber")
    @Mapping(target = "role", source = "customerRole")
    AssignRoleRequest toAssignRoleRequest(SignUpRequest request);
}

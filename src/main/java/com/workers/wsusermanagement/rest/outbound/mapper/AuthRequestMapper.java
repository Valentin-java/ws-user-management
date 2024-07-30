package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;
import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthRequestMapper {

    @Mapping(target = "username", source = "request.userProfile.username")
    @Mapping(target = "password", source = "request.password")
    @Mapping(target = "otp", constant = "false")
    AuthRequest toAuthRequest(ChangePasswordContext request);

    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "otp", constant = "true")
    AuthRequest toAuthRequest(VerifySignUpContext request);

    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "otp", constant = "true")
    AuthRequest toAuthRequest(ConfirmationOtpContext request);

    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "otp", constant = "true")
    AuthRequest toAuthRequest(VerifySignInContext request);
}

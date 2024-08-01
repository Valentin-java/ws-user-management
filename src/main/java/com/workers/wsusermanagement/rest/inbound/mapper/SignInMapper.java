package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInByOtpContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInByPassContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.PasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = UsernameFormatter.class)
public interface SignInMapper {

    @Mapping(target = "request.phoneNumber", source = "phoneNumber", qualifiedByName = "FormatPhoneNumber")
    @Mapping(target = "signUpResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    SignInContext toServiceContext(LoginUserDtoRequest request);

    @Mapping(target = "request.uuid", source = "uuid")
    @Mapping(target = "request.otp", source = "otp")
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    SignInByOtpContext toSignInByOtpContext(OtpRequest request);

    @Mapping(target = "request.phoneNumber", source = "phoneNumber", qualifiedByName = "FormatPhoneNumber")
    @Mapping(target = "request.password", source = "password")
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    SignInByPassContext toSignInByPassContext(PasswordRequest request);

}

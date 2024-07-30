package com.workers.wsusermanagement.bussines.service.setpass.mapper;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.signin.context.VerifySignInContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface ChangeContextMapper {

    @Mapping(target = "request", ignore = true)
    @Mapping(target = "authRequest", source = ".", qualifiedByName = "getAuthRequest")
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    VerifySignInContext toVerifiedSignIn(ChangePasswordContext source);

    @Named("getAuthRequest")
    default AuthRequest getAuthRequest(ChangePasswordContext request) {
        return new AuthRequest(request.getUserProfile().getUsername(), request.getRequest().password(), false);
    }
}

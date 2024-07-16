package com.workers.wsusermanagement.bussines.service.setpass.mapper;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ChangeContextMapper {

    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    SignInContext toSignIn(ChangePasswordContext source);
}

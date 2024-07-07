package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignInRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface SignInMapper {

    @Mapping(target = "signInRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "signInRequest.password", source = "password")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    SignInContext toLoginCustomerContext(CustomerSignInRequest request);
}

package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ChangePasswordMapper {

    @Mapping(target = "request.phoneNumber", source = "phoneNumber")
    @Mapping(target = "request.password", source = "password")
    @Mapping(target = "request.activityStatus", ignore = true)
    @Mapping(target = "request.customerRole", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "notificationMsg", ignore = true)
    ChangePasswordContext toServiceContext(UserSignUpRequest request);
}

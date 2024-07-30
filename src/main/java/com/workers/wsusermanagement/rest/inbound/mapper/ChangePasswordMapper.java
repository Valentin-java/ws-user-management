package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.ChangePasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ChangePasswordMapper {

    @Mapping(target = "request.uuid", source = "uuid")
    @Mapping(target = "request.password", source = "password")
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "notificationMsg", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    ChangePasswordContext toServiceContext(ChangePasswordRequest request);
}

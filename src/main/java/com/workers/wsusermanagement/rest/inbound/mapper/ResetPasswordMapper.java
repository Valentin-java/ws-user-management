package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.ResetPasswordInitRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ResetPasswordMapper {

    @Mapping(target = "request.phoneNumber", source = "phoneNumber")
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    ResetPasswordContext toServiceContext(ResetPasswordInitRequest request);
}

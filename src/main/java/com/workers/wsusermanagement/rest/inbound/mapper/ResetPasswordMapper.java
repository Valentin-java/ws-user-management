package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ResetPasswordMapper {

    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otp", ignore = true)
    ResetPasswordContext toServiceContext(ResetUserPasswordRequest request);

}

package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface NotificationRequestMapper {

    @Mapping(target = "phoneNumber", source = "resetPasswordRequest.phoneNumber")
    @Mapping(target = "text", source = "otp")
    NotificationMessage toNotificationRequest(ResetPasswordContext ctx);
}

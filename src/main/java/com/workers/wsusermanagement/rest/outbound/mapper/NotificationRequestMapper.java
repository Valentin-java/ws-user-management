package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.setpass.context.ChangePasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.outbound.model.NotificationMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface NotificationRequestMapper {

    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    @Mapping(target = "message", source = "otp")
    NotificationMessage toNotificationRequest(ResetPasswordContext ctx);

    @Mapping(target = "phoneNumber", source = "request.phoneNumber")
    @Mapping(target = "message", source = "notificationMsg")
    NotificationMessage toNotificationRequest(ChangePasswordContext ctx);
}

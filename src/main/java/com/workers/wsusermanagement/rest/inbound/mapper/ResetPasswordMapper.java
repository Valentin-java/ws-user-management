package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface ResetPasswordMapper {

    @Mapping(target = "resetPasswordRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "resetPasswordRequest.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otp", ignore = true)
    ResetPasswordContext toServiceContext(ResetUserPasswordRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus(ResetUserPasswordRequest request) {
        return ActivityStatus.INACTIVE;
    }
}
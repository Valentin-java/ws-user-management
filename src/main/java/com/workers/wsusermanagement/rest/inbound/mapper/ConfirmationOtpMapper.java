package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface ConfirmationOtpMapper {

    @Mapping(target = "otpRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "otpRequest.otp", source = "otp")
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "resetPasswordRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "resetPasswordRequest.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    ConfirmationOtpContext toServiceContext(OtpRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus(ResetUserPasswordRequest request) {
        return ActivityStatus.INACTIVE;
    }
}

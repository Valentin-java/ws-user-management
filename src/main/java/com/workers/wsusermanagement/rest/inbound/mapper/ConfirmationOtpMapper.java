package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.confirmotp.context.ConfirmationOtpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface ConfirmationOtpMapper {

    @Mapping(target = "request.uuid", source = "uuid")
    @Mapping(target = "request.otp", source = "otp")
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    @Mapping(target = "authRequest", ignore = true)
    ConfirmationOtpContext toServiceContext(OtpRequest request);
}

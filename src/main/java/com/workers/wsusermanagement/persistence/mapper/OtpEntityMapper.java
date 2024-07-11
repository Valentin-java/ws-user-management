package com.workers.wsusermanagement.persistence.mapper;

import com.workers.wsusermanagement.bussines.service.reset.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface OtpEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "otp", source = "otp")
    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "activityStatus", source = "resetPasswordRequest.activityStatus")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OtpEntity toEntity(ResetPasswordContext ctx);
}

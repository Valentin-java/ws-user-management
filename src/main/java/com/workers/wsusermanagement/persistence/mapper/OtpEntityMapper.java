package com.workers.wsusermanagement.persistence.mapper;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.TypeOtp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface OtpEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "otp", source = "otp")
    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "activityStatus", source = "request.activityStatus")
    @Mapping(target = "typeOtp", source = ".", qualifiedByName = "getTypeOtp")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OtpEntity toResetOtpEntity(ResetPasswordContext ctx);

    @Named("getTypeOtp")
    default TypeOtp getTypeOtp(ResetPasswordContext request) {
        return TypeOtp.RESET;
    }
}

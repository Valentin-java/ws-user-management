package com.workers.wsusermanagement.persistence.mapper;

import com.workers.wsusermanagement.bussines.service.resetpass.context.ResetPasswordContext;
import com.workers.wsusermanagement.bussines.service.signin.context.SignInContext;
import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import com.workers.wsusermanagement.persistence.enums.StatusOtp;
import com.workers.wsusermanagement.persistence.enums.TypeOtp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.security.SecureRandom;
import java.util.UUID;

@Mapper(config = MapperConfiguration.class, imports = {TypeOtp.class, StatusOtp.class})
public interface OtpEntityMapper {

    Integer OTP_LENGTH = 4;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(this.generateUUID())")
    @Mapping(target = "otp", expression = "java(this.generateOtp())")
    @Mapping(target = "username", source = "userProfile.username")
    @Mapping(target = "typeOtp", expression = "java(TypeOtp.RESET)")
    @Mapping(target = "statusOtp", expression = "java(StatusOtp.CREATED)")
    @Mapping(target = "createdAt", ignore = true)
    OtpEntity toResetOtpEntity(ResetPasswordContext ctx);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(this.generateUUID())")
    @Mapping(target = "otp", expression = "java(this.generateOtp())")
    @Mapping(target = "username", source = "request.phoneNumber")
    @Mapping(target = "typeOtp", expression = "java(TypeOtp.SIGN_UP)")
    @Mapping(target = "statusOtp", expression = "java(StatusOtp.CREATED)")
    @Mapping(target = "createdAt", ignore = true)
    OtpEntity toSignUpOtpEntity(SignUpContext ctx);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(this.generateUUID())")
    @Mapping(target = "otp", expression = "java(this.generateOtp())")
    @Mapping(target = "username", source = "request.phoneNumber")
    @Mapping(target = "typeOtp", expression = "java(TypeOtp.SIGN_IN)")
    @Mapping(target = "statusOtp", expression = "java(StatusOtp.CREATED)")
    @Mapping(target = "createdAt", ignore = true)
    OtpEntity toSignUpOtpEntity(SignInContext ctx);

    default String generateUUID() {
        return UUID.randomUUID().toString();
    }

    default String generateOtp() {
        var otp = new SecureRandom().nextInt((int) Math.pow(10, OTP_LENGTH));
        return String.format("%04d", otp);
    }
}

package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signup.context.VerifySignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface VerifySignUpMapper {

    @Mapping(target = "request.uuid", source = "uuid")
    @Mapping(target = "request.otp", source = "otp")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest.role", source = ".", qualifiedByName = "getCustomerRole")
    @Mapping(target = "assignRoleRequest.username", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    VerifySignUpContext toCustomerServiceContext(OtpRequest request);

    @Mapping(target = "request.uuid", source = "uuid")
    @Mapping(target = "request.otp", source = "otp")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest.role", source = ".", qualifiedByName = "getHandymanRole")
    @Mapping(target = "assignRoleRequest.username", ignore = true)
    @Mapping(target = "signInResponse", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    VerifySignUpContext toHandymanServiceContext(OtpRequest request);

    @Named("getCustomerRole")
    default CustomerRole getCustomerRole(OtpRequest request) {
        return CustomerRole.CUSTOMER;
    }

    @Named("getHandymanRole")
    default CustomerRole getHandymanRole(OtpRequest request) {
        return CustomerRole.HANDYMAN;
    }
}

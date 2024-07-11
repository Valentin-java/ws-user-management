package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface SignUpMapper {

    @Mapping(target = "signUpRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "signUpRequest.password", source = "password")
    @Mapping(target = "signUpRequest.activityStatus", qualifiedByName = "getActivityStatus")
    @Mapping(target = "signUpRequest.customerRole", qualifiedByName = "getCustomerRole")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest", ignore = true)
    SignUpContext toRegistryCustomerContext(UserSignUpRequest request);

    @Mapping(target = "signUpRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "signUpRequest.password", source = "password")
    @Mapping(target = "signUpRequest.activityStatus", qualifiedByName = "getActivityStatus")
    @Mapping(target = "signUpRequest.customerRole", qualifiedByName = "getHandymanRole")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest", ignore = true)
    SignUpContext toRegistryHandymanContext(UserSignUpRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus() {
        return ActivityStatus.INACTIVE;
    }

    @Named("getCustomerRole")
    default CustomerRole getCustomerRole() {
        return CustomerRole.CUSTOMER;
    }

    @Named("getHandymanRole")
    default CustomerRole getHandymanRole() {
        return CustomerRole.HANDYMAN;
    }
}

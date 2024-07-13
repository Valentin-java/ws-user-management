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

    @Mapping(target = "request.phoneNumber", source = "phoneNumber")
    @Mapping(target = "request.password", source = "password")
    @Mapping(target = "request.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "request.customerRole", source = ".", qualifiedByName = "getCustomerRole")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest", ignore = true)
    SignUpContext toCustomerServiceContext(UserSignUpRequest request);

    @Mapping(target = "request.phoneNumber", source = "phoneNumber")
    @Mapping(target = "request.password", source = "password")
    @Mapping(target = "request.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "request.customerRole", source = ".", qualifiedByName = "getHandymanRole")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest", ignore = true)
    SignUpContext toHandymanServiceContext(UserSignUpRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus(UserSignUpRequest request) {
        return ActivityStatus.INACTIVE;
    }

    @Named("getCustomerRole")
    default CustomerRole getCustomerRole(UserSignUpRequest request) {
        return CustomerRole.CUSTOMER;
    }

    @Named("getHandymanRole")
    default CustomerRole getHandymanRole(UserSignUpRequest request) {
        return CustomerRole.HANDYMAN;
    }
}

package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;
import com.workers.wsusermanagement.rest.inbound.dto.CustomerSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class)
public interface SignUpMapper {

    @Mapping(target = "signUpRequest.phoneNumber", source = "phoneNumber")
    @Mapping(target = "signUpRequest.password", source = "password")
    @Mapping(target = "signUpRequest.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "signUpRequest.customerRole", source = ".", qualifiedByName = "getCustomerRole")
    @Mapping(target = "authRequest", ignore = true)
    @Mapping(target = "assignRoleRequest", ignore = true)
    SignUpContext toRegistryCustomerContext(CustomerSignUpRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus(CustomerSignUpRequest request) {
        return ActivityStatus.INACTIVE;
    }

    @Named("getCustomerRole")
    default CustomerRole getCustomerRole(CustomerSignUpRequest request) {
        return CustomerRole.CUSTOMER;
    }
}

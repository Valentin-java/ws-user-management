package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.bussines.service.signup.context.SignUpContext;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.enums.ActivityStatus;
import com.workers.wsusermanagement.persistence.enums.CustomerRole;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfiguration.class, uses = UsernameFormatter.class)
public interface SignUpMapper {

    @Mapping(target = "request.phoneNumber", source = "phoneNumber", qualifiedByName = "FormatPhoneNumber")
    @Mapping(target = "request.firstName", source = "firstName")
    @Mapping(target = "request.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "request.customerRole", source = ".", qualifiedByName = "getCustomerRole")
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    SignUpContext toCustomerServiceContext(RegistryUserDtoRequest request);

    @Mapping(target = "request.phoneNumber", source = "phoneNumber", qualifiedByName = "FormatPhoneNumber")
    @Mapping(target = "request.firstName", source = "firstName")
    @Mapping(target = "request.activityStatus", source = ".", qualifiedByName = "getActivityStatus")
    @Mapping(target = "request.customerRole", source = ".", qualifiedByName = "getHandymanRole")
    @Mapping(target = "notificationRequest", ignore = true)
    @Mapping(target = "otpEntity", ignore = true)
    SignUpContext toHandymanServiceContext(RegistryUserDtoRequest request);

    @Named("getActivityStatus")
    default ActivityStatus getActivityStatus(RegistryUserDtoRequest request) {
        return ActivityStatus.INACTIVE;
    }

    @Named("getCustomerRole")
    default CustomerRole getCustomerRole(RegistryUserDtoRequest request) {
        return CustomerRole.CUSTOMER;
    }

    @Named("getHandymanRole")
    default CustomerRole getHandymanRole(RegistryUserDtoRequest request) {
        return CustomerRole.HANDYMAN;
    }
}

package com.workers.wsusermanagement.persistence.mapper;

import com.workers.wsusermanagement.bussines.service.signup.model.RegistryUserRequest;
import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface CustomerMapper {

    @Mapping(target = "username", source = "phoneNumber")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "textError", ignore = true)
    @Mapping(target = "lastVisitAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "otp", constant = "true")
    UserProfile toEntity(RegistryUserRequest request);
}

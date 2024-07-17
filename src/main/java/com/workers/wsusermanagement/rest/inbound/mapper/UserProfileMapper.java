package com.workers.wsusermanagement.rest.inbound.mapper;

import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.persistence.entity.UserProfile;
import com.workers.wsusermanagement.rest.inbound.dto.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface UserProfileMapper {

    @Mapping(target = "phoneNumber", source = "username")
    UserProfileResponse toRest(UserProfile source);
}

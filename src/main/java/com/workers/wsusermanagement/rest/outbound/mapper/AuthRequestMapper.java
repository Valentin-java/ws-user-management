package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthRequestMapper {

    @Mapping(target = "username", source = "phoneNumber")
    AuthRequest toRest(SignUpRequest request);
}

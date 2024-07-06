package com.workers.wsusermanagement.rest.outbound.mapper;

import com.workers.wsusermanagement.config.mapper.MapperConfiguration;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import com.workers.wsusermanagement.rest.outbound.feign.dto.AssignRoleRequest;
import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface AuthRequestMapper {

    @Mapping(target = "username", source = "phoneNumber")
    AuthRequest toAuthRequest(SignUpRequest request);

    @Mapping(target = "username", source = "phoneNumber")
    @Mapping(target = "role", source = "customerRole")
    AssignRoleRequest toAssignRoleRequest(SignUpRequest request);
}

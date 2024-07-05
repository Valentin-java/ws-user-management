package com.workers.wsusermanagement.rest.outbound.feign.client;

import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;

public interface WsAuthFeignClient {

    Boolean requestToRegistryCustomer(SignUpRequest request);
}

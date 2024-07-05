package com.workers.wsusermanagement.rest.outbound.feign.controller;

import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "WsAuthFeign", url = "${feign.ws-auth.url}", path = "/v1")
public interface WsAuthFeign {

    @PostMapping(value = "/workers/auth/register")
    ResponseEntity<Boolean> registerCustomer(@RequestBody AuthRequest request);
}

package com.workers.wsusermanagement.rest.outbound.feign;

import com.workers.wsusermanagement.rest.outbound.model.AssignRoleRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "WsAuthFeign", url = "${feign.ws-auth.url}", path = "/v1")
public interface WsAuthFeign {

    @PostMapping(value = "/workers/auth/register")
    ResponseEntity<Boolean> registerCustomer(@RequestBody AuthRequest request);

    @PostMapping(value = "/workers/auth/assign-role")
    ResponseEntity<String> assignRole(@RequestBody AssignRoleRequest request);

    @PostMapping(value = "/workers/auth/activation")
    ResponseEntity<AuthResponse> activationCustomer(@RequestBody AuthRequest request);
}
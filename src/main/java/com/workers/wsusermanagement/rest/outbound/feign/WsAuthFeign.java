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
    ResponseEntity<Void> registerCustomer(@RequestBody AuthRequest request);

    @PostMapping(value = "/workers/auth/assign-role")
    ResponseEntity<Void> assignRole(@RequestBody AssignRoleRequest request);

    @PostMapping(value = "/workers/auth/activation")
    ResponseEntity<Void> activationCustomer(@RequestBody AuthRequest request);

    @PostMapping(value = "/workers/auth/login")
    ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest request);

    @PostMapping(value = "/workers/auth/reset")
    ResponseEntity<Void> requestToResetPassword(@RequestBody AuthRequest request);

    @PostMapping(value = "/workers/auth/changepass")
    ResponseEntity<Void> requestToChangePassword(@RequestBody AuthRequest request);
}

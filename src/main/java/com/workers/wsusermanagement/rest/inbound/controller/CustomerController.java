package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.CustomerService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/workers/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(customerService.signingUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(customerService.signingUp(request));
    }

    @PostMapping("/otp")
    public void validateOtp(@RequestBody OtpRequest request) {
        customerService.validateOtp(request);
    }
}
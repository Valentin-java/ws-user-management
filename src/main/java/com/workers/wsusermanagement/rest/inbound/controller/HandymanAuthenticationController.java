package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.HandymanAuthenticationService;
import com.workers.wsusermanagement.rest.inbound.dto.LoginUserDtoRequest;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.RegistryUserDtoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/handyman")
public class HandymanAuthenticationController {

    private final HandymanAuthenticationService handymanAuthenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody RegistryUserDtoRequest request) {
        return ResponseEntity.ok(handymanAuthenticationService.signUp(request));
    }

    @PostMapping("/verify/sign-up")
    public ResponseEntity<?> verifySignUp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(handymanAuthenticationService.verifySignUp(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody LoginUserDtoRequest request) {
        return ResponseEntity.ok(handymanAuthenticationService.signIn(request));
    }

    @PostMapping("/verify/sign-in")
    public ResponseEntity<?> verifySignIn(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(handymanAuthenticationService.signInByOtp(request));
    }
}

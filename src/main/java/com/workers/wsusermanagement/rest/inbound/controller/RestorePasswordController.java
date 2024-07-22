package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.rest.inbound.dto.OtpRequest;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/restore")
public class RestorePasswordController {

    private final RestorePasswordService restorePasswordService;

    @PostMapping("/reset")
    public ResponseEntity<?> resetPasswordByOtp(@RequestBody ResetUserPasswordRequest request) {
        return ResponseEntity.ok(restorePasswordService.resetPasswordByOtp(request));
    }

    @PostMapping("/otp")
    public ResponseEntity<?> getConfirmationByOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(restorePasswordService.getConfirmationOtp(request));
    }

    @PostMapping("/setpass")
    public ResponseEntity<?> setPasswordByOtp(@RequestBody UserSignUpRequest request) {
        return ResponseEntity.ok(restorePasswordService.setPasswordByOtp(request));
    }
}

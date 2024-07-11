package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.RestorePasswordService;
import com.workers.wsusermanagement.rest.inbound.dto.ResetUserPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/workers/restore")
public class RestorePasswordController {

    private final RestorePasswordService restorePasswordService;

    @PostMapping("/reset")
    public ResponseEntity<?> resetPasswordByOtp(@RequestBody ResetUserPasswordRequest request) {
        return ResponseEntity.ok(restorePasswordService.resetPasswordByOtp(request));
    }


}

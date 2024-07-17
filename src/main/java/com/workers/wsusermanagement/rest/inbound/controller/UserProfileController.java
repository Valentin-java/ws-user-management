package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/workers/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getProfileData(@PathVariable(value = "id") String phoneNumber) {
        return ResponseEntity.ok(userProfileService.getProfileData(phoneNumber));
    }
}

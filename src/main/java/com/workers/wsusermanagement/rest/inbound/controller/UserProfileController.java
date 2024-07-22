package com.workers.wsusermanagement.rest.inbound.controller;

import com.workers.wsusermanagement.bussines.interfaces.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getProfileData(@PathVariable(value = "id") String phoneNumber) {
        log.debug("[UserProfile] getProfileData with userId: {}", phoneNumber);
        return ResponseEntity.ok(userProfileService.getProfileData(phoneNumber));
    }

//    @PutMapping("/info/{id}")
//    public ResponseEntity<?> updateProfileData(UserProfileRequest request) {
//        return ResponseEntity.ok(userProfileService.updateProfileData(request));
//    }
}

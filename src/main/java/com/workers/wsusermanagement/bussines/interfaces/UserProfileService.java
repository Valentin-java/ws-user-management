package com.workers.wsusermanagement.bussines.interfaces;

import com.workers.wsusermanagement.rest.inbound.dto.UserProfileRequest;
import com.workers.wsusermanagement.rest.inbound.dto.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse getProfileData(String phoneNumber);

    Boolean updateProfileData(UserProfileRequest request);
}

package com.workers.wsusermanagement.persistence.repository;

import com.workers.wsusermanagement.persistence.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Boolean existsUserProfileByUsername(String username);

    Optional<UserProfile> findByUsername(String username);
}

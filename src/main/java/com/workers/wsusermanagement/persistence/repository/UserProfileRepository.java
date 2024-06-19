package com.workers.wsusermanagement.persistence.repository;

import com.workers.wsusermanagement.persistence.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}

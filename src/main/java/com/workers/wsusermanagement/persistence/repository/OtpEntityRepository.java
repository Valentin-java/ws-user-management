package com.workers.wsusermanagement.persistence.repository;

import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtpEntityRepository extends JpaRepository<OtpEntity, Long>  {

    List<OtpEntity> findAllByUsername(String username);
}

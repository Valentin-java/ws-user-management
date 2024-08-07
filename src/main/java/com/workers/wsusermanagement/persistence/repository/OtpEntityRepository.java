package com.workers.wsusermanagement.persistence.repository;

import com.workers.wsusermanagement.persistence.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OtpEntityRepository extends JpaRepository<OtpEntity, Long>  {

    List<OtpEntity> findAllByUsername(String username);

    Optional<OtpEntity> findAllByUuid(String uuid);

    void deleteAllByUsername(String username);
}

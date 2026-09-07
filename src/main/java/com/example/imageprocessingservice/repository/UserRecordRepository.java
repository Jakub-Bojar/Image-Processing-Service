package com.example.imageprocessingservice.repository;

import com.example.imageprocessingservice.domain.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long>{
    Optional<UserRecord> findByEmail(String email);
}

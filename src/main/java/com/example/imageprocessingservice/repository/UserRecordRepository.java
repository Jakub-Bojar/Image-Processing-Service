package com.example.imageprocessingservice.repository;

import com.example.imageprocessingservice.domain.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecordRepository extends JpaRepository<UserRecord, Long>{
}

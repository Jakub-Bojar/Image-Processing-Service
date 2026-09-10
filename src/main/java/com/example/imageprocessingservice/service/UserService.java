package com.example.imageprocessingservice.service;

import com.example.imageprocessingservice.domain.UserRecord;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(UserRecord user);
}

package com.example.imageprocessingservice.service;

import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRecordRepository userRecordRepository;

    @Override
    public boolean existsByUsername(String username) {
        return userRecordRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRecordRepository.existsByUsername(email);
    }

    @Override
    public void save(UserRecord user) {
        userRecordRepository.save(user);
    }
}


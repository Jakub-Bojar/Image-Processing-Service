package com.example.imageprocessingservice.api.controller;

import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserRecordRepository repository;

    public AuthenticationController(UserRecordRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/register")
    public UserRecord create(@RequestParam String name, @RequestParam String password) {
        return repository.save(new UserRecord(name, password));
    }
}

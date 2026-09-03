package com.example.imageprocessingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private Instant createdAt = Instant.now();

    protected UserRecord() {
        // required by JPA
    }

    public UserRecord(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() { return name; }

    public Instant getCreatedAt() {
        return createdAt;
    }


}

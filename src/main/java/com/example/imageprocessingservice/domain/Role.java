package com.example.imageprocessingservice.domain;

import jakarta.persistence.*;
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    public Role() {
    }

    public Role(Integer id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() { return id; }
    public ERole getName() { return name; }
    public void setName(ERole name) { this.name = name; }
}

package com.example.imageprocessingservice.dto;

public record SignInResponseDto(
        Long id,
        String username,
        String email,
        String token
) {
}

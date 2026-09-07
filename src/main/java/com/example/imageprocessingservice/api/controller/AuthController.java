package com.example.imageprocessingservice.api.controller;
import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import com.example.imageprocessingservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRecordRepository userRecordRepository;


    @PostMapping("/login")
    public String login(@RequestBody UserRecord userRecord) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRecord.getEmail(), userRecord.getPassword())
        );
        return jwtUtil.generateToken(userRecord.getEmail());
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRecord userRecord) {
        userRecordRepository.save(userRecord);
        return "User registered successfully!";
    }
}

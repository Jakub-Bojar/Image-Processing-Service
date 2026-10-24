package com.example.imageprocessingservice.api.controller;

import com.example.imageprocessingservice.domain.ERole;
import com.example.imageprocessingservice.domain.Role;
import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.dto.ApiResponseDto;
import com.example.imageprocessingservice.dto.SignInRequestDto;
import com.example.imageprocessingservice.dto.SignInResponseDto;
import com.example.imageprocessingservice.dto.SignUpRequestDto;
import com.example.imageprocessingservice.repository.RoleRepository;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import com.example.imageprocessingservice.security.UserDetailsImpl;
import com.example.imageprocessingservice.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRecordRepository userRecordRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRecordRepository userRecordRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRecordRepository = userRecordRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ApiResponseDto<Void>> register(@Valid @RequestBody SignUpRequestDto request) {
        if (userRecordRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto<>(false, "Username is already in use", null));
        }
        if (userRecordRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto<>(false, "Email is already in use", null));
        }

        Role userRole = roleRepository.findByName(ERole.ROLE_USER);
        if (userRole == null) {
            userRole = roleRepository.save(new Role(ERole.ROLE_USER));
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        UserRecord user = new UserRecord(
                null,
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                true,
                roles
        );
        userRecordRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(true, "User registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<SignInResponseDto>> login(@Valid @RequestBody SignInRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateJwtToken(authentication);
        SignInResponseDto response = new SignInResponseDto(user.getId(), user.getUsername(), user.getEmail(), token);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Login successful", response));
    }
}

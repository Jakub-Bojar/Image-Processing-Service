package com.example.imageprocessingservice.service;
import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRecordRepository userRecordRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserRecord userRecord = userRecordRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                userRecord.getEmail(),
                userRecord.getPassword(),
                Collections.emptyList() // You can add roles / authorities here
        );
    }
}
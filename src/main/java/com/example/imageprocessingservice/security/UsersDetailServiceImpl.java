package com.example.imageprocessingservice.security;

import com.example.imageprocessingservice.domain.UserRecord;
import com.example.imageprocessingservice.repository.UserRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailServiceImpl  implements UserDetailsService {
    @Autowired
    UserRecordRepository userRecordRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRecord userRecord = userRecordRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
        return UserDetailsImpl.build(userRecord);
    }
}

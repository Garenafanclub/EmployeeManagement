package com.example.EmpManagement.Service.jwtSecurity;

import com.example.EmpManagement.Repository.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee is not created with email: " + email));
    }
}

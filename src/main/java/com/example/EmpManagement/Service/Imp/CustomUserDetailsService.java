package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.Model.Provider;
import com.example.EmpManagement.Model.User;
import com.example.EmpManagement.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Look up the security badge in the decoupled users table
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Security identity not found for email: " + email));

        // 2. Hand the badge to Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(String.valueOf(Provider.USER))
                .build();
    }
}

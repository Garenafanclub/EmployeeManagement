package com.example.EmpManagement.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class HashedPass {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPass = "Admin@123";
        String hashedPassword = encoder.encode(rawPass);

        log.info("ADMIN ONE TIME PASS: {}", hashedPassword);
    }
}

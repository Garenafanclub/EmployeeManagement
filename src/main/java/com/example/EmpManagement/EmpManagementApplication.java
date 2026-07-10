package com.example.EmpManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmpManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpManagementApplication.class, args);
	}

}

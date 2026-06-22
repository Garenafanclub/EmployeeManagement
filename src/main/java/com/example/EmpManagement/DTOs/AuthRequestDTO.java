package com.example.EmpManagement.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDTO {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Must be a valid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;;
}

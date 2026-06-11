package com.example.EmpManagement.DTOs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PFRequestDTO {

    @NotBlank(message = "UAN Number is required")
    private String uanNumber;

    @NotBlank(message = "PF Number is required")
    private String pfNumber;

    private Double currentBalance;

    @NotNull(message = "Employee ID is required to link this account")
    private Long employeeId;
}

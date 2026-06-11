package com.example.EmpManagement.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PFResponseDTO {

    private Long id;
    private String pfNumber;
    private Double currentBalance;
}

package com.example.EmpManagement.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PFResponseDTO implements Serializable {

    private Long id;
    private String pfNumber;
    private Double currentBalance;
}

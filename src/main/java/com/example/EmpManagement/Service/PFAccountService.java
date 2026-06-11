package com.example.EmpManagement.Service;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;

public interface PFAccountService {
    PFResponseDTO createdPFAccount(PFRequestDTO requestDTO);
}

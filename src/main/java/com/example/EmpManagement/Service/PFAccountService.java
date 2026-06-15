package com.example.EmpManagement.Service;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;

import java.util.List;

public interface PFAccountService {
    PFResponseDTO createdPFAccount(PFRequestDTO requestDTO);

    List<PFResponseDTO> getAllPfAccounts();
}

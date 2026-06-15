package com.example.EmpManagement.Service;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DepService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO);

    List<DepartmentResponseDTO> getAllDepartment();
}

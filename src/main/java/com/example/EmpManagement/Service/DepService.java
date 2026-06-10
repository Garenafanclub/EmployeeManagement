package com.example.EmpManagement.Service;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;

public interface DepService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO);
}

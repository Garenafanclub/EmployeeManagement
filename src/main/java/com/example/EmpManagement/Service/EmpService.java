package com.example.EmpManagement.Service;

import com.example.EmpManagement.DTOs.EmployeeRequestDTO;
import com.example.EmpManagement.DTOs.EmployeeResponseDTO;
import com.example.EmpManagement.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmpService {

    Page<EmployeeResponseDTO> getAllEmp(int page, int size, String sortBy, String direction);

    EmployeeResponseDTO getById(Long id);

    EmployeeResponseDTO getEmpByEmail(String email);

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);

    EmployeeResponseDTO updateEmployee(EmployeeRequestDTO employee, Long id);

    void deleteEmpById(Long id);
}


// duplication , null checks, validation and exception handling..., pagination...
package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import com.example.EmpManagement.Mapper.DepartmentMapper;
import com.example.EmpManagement.Model.Department;
import com.example.EmpManagement.Repository.DepRepo;
import com.example.EmpManagement.Service.DepService;
import org.springframework.stereotype.Service;

@Service
public class DepServiceImp implements DepService {

    private final DepRepo depRepo;
    private final DepartmentMapper departmentMapper;

    public DepServiceImp(DepRepo depRepo, DepartmentMapper departmentMapper) {
        this.depRepo = depRepo;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = departmentMapper.toEntity(departmentRequestDTO);

        Department savedDepartment = depRepo.save(department);

        return departmentMapper.toResponseDTO(savedDepartment);
    }
}

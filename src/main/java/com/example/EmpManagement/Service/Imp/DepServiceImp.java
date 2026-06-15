package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import com.example.EmpManagement.Mapper.DepartmentMapper;
import com.example.EmpManagement.Model.Department;
import com.example.EmpManagement.Repository.DepRepo;
import com.example.EmpManagement.Service.DepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepServiceImp implements DepService {

    private final DepRepo depRepo;
    private final DepartmentMapper departmentMapper;

    public DepServiceImp(DepRepo depRepo, DepartmentMapper departmentMapper) {
        this.depRepo = depRepo;
        this.departmentMapper = departmentMapper;
    }

    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = departmentMapper.toEntity(departmentRequestDTO);

        Department savedDepartment = depRepo.save(department);

        return departmentMapper.toResponseDTO(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartment() {
        List<Department> department = depRepo.findAll();
        return department.stream()
                .map(departmentMapper::toResponseDTO)
                .toList();
    }
}

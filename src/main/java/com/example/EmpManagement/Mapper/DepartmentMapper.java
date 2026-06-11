package com.example.EmpManagement.Mapper;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import com.example.EmpManagement.Model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // FRONTEND TO BACKEND...
    // JSON -> DepartmentRequestDTO -> Entity (Department)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    Department toEntity(DepartmentRequestDTO requestDTO);

    // Backend to Frontend...
    // Map Entity ---> responseDTO
    DepartmentResponseDTO toResponseDTO(Department department);


    // Update Mapping...
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateEntityFromDto(DepartmentRequestDTO dto, @MappingTarget Department entity);
}

package com.example.EmpManagement.Mapper;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;
import com.example.EmpManagement.Model.PfAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PfAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)  // Handled in service layer...
    PfAccount toEntity(PFRequestDTO requestDTO);


    PFResponseDTO toResponseDTO(PfAccount pfAccount);

}

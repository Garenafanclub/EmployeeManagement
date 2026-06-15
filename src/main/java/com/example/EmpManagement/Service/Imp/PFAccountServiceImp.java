package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;
import com.example.EmpManagement.Exceptions.ResourceNotFoundException;
import com.example.EmpManagement.Mapper.PfAccountMapper;
import com.example.EmpManagement.Model.Department;
import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Model.PfAccount;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Repository.PfAccountRepo;
import com.example.EmpManagement.Service.PFAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PFAccountServiceImp implements PFAccountService {

    private final PfAccountMapper pfAccountMapper;
    private final PfAccountRepo pfAccountRepo;
    private final EmpRepo empRepo;

    public PFAccountServiceImp(PfAccountMapper pfAccountMapper, PfAccountRepo pfAccountRepo, EmpRepo empRepo) {
        this.pfAccountMapper = pfAccountMapper;
        this.pfAccountRepo = pfAccountRepo;
        this.empRepo = empRepo;
    }

    @Override
    public PFResponseDTO createdPFAccount(PFRequestDTO requestDTO) {

        Employee employee = empRepo.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", requestDTO.getEmployeeId()));

        PfAccount pfAccount = pfAccountMapper.toEntity(requestDTO);

        pfAccount.setEmployee(employee);

        PfAccount savedPf = pfAccountRepo.save(pfAccount);

        return pfAccountMapper.toResponseDTO(savedPf);
    }

    @Override
    public List<PFResponseDTO> getAllPfAccounts() {
        List<PfAccount> pfAccountList = pfAccountRepo.findAll();
        return pfAccountList.stream()
                .map(pfAccountMapper::toResponseDTO)
                .toList();
    }
}

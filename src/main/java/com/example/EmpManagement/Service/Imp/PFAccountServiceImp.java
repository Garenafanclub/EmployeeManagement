package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;
import com.example.EmpManagement.Mapper.PfAccountMapper;
import com.example.EmpManagement.Model.PfAccount;
import com.example.EmpManagement.Repository.PfAccountRepo;
import com.example.EmpManagement.Service.PFAccountService;
import org.springframework.stereotype.Service;

@Service
public class PFAccountServiceImp implements PFAccountService {

    private final PfAccountMapper pfAccountMapper;
    private final PfAccountRepo pfAccountRepo;

    public PFAccountServiceImp(PfAccountMapper pfAccountMapper, PfAccountRepo pfAccountRepo) {
        this.pfAccountMapper = pfAccountMapper;
        this.pfAccountRepo = pfAccountRepo;
    }

    @Override
    public PFResponseDTO createdPFAccount(PFRequestDTO requestDTO) {
        PfAccount pfAccount = pfAccountMapper.toEntity(requestDTO);

        PfAccount savedPf = pfAccountRepo.save(pfAccount);

        return pfAccountMapper.toResponseDTO(savedPf);
    }
}

package com.example.EmpManagement.Controller;

import com.example.EmpManagement.DTOs.PFRequestDTO;
import com.example.EmpManagement.DTOs.PFResponseDTO;
import com.example.EmpManagement.Service.PFAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/pfaccounts")
public class PfAccountController {

    private final PFAccountService pfAccountService;

    public PfAccountController(PFAccountService pfAccountService) {
        this.pfAccountService = pfAccountService;
    }

    @PostMapping()
    public ResponseEntity<PFResponseDTO> createPFAccount(@Valid @RequestBody PFRequestDTO requestDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(pfAccountService.createdPFAccount(requestDTO));
    }

}

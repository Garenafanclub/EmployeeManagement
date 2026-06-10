package com.example.EmpManagement.Controller;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import com.example.EmpManagement.Service.DepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/department")
public class DepController {

    private final DepService departmentService;

    public DepController(DepService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {

        DepartmentResponseDTO savedDepartment = departmentService.createDepartment(departmentRequestDTO);

        // Dynamically build the URI for the newly created department
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDepartment.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedDepartment);
    }

}

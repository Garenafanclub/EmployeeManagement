package com.example.EmpManagement.Controller;

import com.example.EmpManagement.DTOs.DepartmentRequestDTO;
import com.example.EmpManagement.DTOs.DepartmentResponseDTO;
import com.example.EmpManagement.Service.DepService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${api.version}/departments")
public class DepController {

    private final DepService departmentService;

    public DepController(DepService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) {

        DepartmentResponseDTO savedDepartment = departmentService.createDepartment(departmentRequestDTO);

        // Dynamically build the URI for the newly created department

        /*
             name : "java",
             des : "java r/d team"
         */
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDepartment.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedDepartment);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartment()
    {
       return ResponseEntity.ok(departmentService.getAllDepartment());
    }

}

package com.example.EmpManagement.Controller;

import com.example.EmpManagement.DTOs.EmployeeRequestDTO;
import com.example.EmpManagement.DTOs.EmployeeResponseDTO;
import com.example.EmpManagement.Service.EmpService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.version}/employees")
public class EmpController {

    private final EmpService empService;

    public EmpController(EmpService empService)
    {
        this.empService = empService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmp(
            @RequestParam(defaultValue = "0", required = false)    int PageNumber,
            @RequestParam(defaultValue = "2", required = false)   int PageSize,
            @RequestParam(defaultValue = "id", required = false)   String sortBy,
            @RequestParam(defaultValue = "asc", required = false)  String direction)
    {
        return ResponseEntity.ok(empService.getAllEmp(PageNumber, PageSize, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmpById(@PathVariable Long id)
    {
        return ResponseEntity.ok(empService.getById(id));
    }

    @GetMapping("/{email}")
    public ResponseEntity<EmployeeResponseDTO> getEmpByEmail(@Valid @PathVariable String email)
    {
        return ResponseEntity.ok(empService.getEmpByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO)
    {
         return ResponseEntity.status(HttpStatus.CREATED).body(empService.createEmployee(employeeRequestDTO));
    }

    @PatchMapping("{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@Valid @RequestBody EmployeeRequestDTO employee, @PathVariable Long id)
    {
        return ResponseEntity.ok(empService.updateEmployee(employee, id));
    }

    @DeleteMapping("/{id}")
    public String deleteEmp(@PathVariable Long id)
    {
        empService.deleteEmpById(id);
        return "Successfully remove from the record..";
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDTO>> searchByName(
            @RequestParam("letter") String letter,
            @RequestParam(defaultValue = "0") int PageNumber,
            @RequestParam(defaultValue = "2") int PageSize)
    {
        return ResponseEntity.ok(empService.searchEmployeeByName(letter, PageNumber, PageSize));
    }

}

package com.example.EmpManagement.Controller;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Service.EmpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmpController {

    private final EmpService empService;

    public EmpController(EmpService empService)
    {
        this.empService = empService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmp()
    {
        return ResponseEntity.ok(empService.getAllEmp());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Employee> getEmpById(@PathVariable Long id)
    {
        return ResponseEntity.ok(empService.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> getEmpByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(empService.getEmpByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee)
    {
         return ResponseEntity.status(HttpStatus.CREATED).body(empService.createEmployee(employee));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable Long id)
    {
        return ResponseEntity.ok(empService.updateEmployee(employee, id));
    }

    @DeleteMapping("/id/{id}")
    public String deleteEmp(@PathVariable Long id)
    {
        empService.deleteEmpById(id);
        return "Successfully remove from the record..";
    }

}

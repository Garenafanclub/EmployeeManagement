package com.example.EmpManagement.Service;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpServiceImp implements EmpService{

    private final EmpRepo empRepo;

    @Override
    public List<Employee> getAllEmp() {
        return empRepo.findAll();
    }

    @Override
    public Employee getById(Long id) {
        return empRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Emp not found with id: " + id));
    }

    @Override
    public Employee getEmpByEmail(String email) {
        return empRepo.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Emp not found with email: " + email));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return empRepo.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, Long id) {

            Employee existEmp = empRepo.findById(id)
                    .orElseThrow(()-> new RuntimeException("Emp not found with id: " + id));

            existEmp.setFirstName(employee.getFirstName());
            existEmp.setLastName(employee.getLastName());
            existEmp.setEmail(employee.getEmail());
            existEmp.setDepartment(employee.getDepartment());
            existEmp.setSalary(employee.getSalary());
            return empRepo.save(existEmp);
    }

    @Override
    public void deleteEmpById(Long id) {
        Employee existEmp = empRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Emp not found with id: " + id));
        empRepo.deleteById(id);
    }
}

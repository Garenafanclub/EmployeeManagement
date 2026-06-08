package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.EmpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class EmpServiceImp implements EmpService {

    private final EmpRepo empRepo;

    public EmpServiceImp(EmpRepo empRepo)
    {
        this.empRepo = empRepo;
    }

    @Override
    public List<Employee> getAllEmp() {
        return empRepo.findAll();
    }

    @Override
    public Employee getById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return empRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Emp not found with id: " + id));
    }

    @Override
    public Employee getEmpByEmail(String email) {
        log.info("Fetching employee with email: {}", email);
        return empRepo.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Emp not found with email: " + email));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if(empRepo.findByEmail(employee.getEmail()).isPresent())
        {
            throw new RuntimeException("Emp already exists with email: " + employee.getEmail());
        }
        log.info("Creating employee with email: {}", employee.getEmail());
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

package com.example.EmpManagement.Service;

import com.example.EmpManagement.Model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmpService {

    List<Employee> getAllEmp();

    Employee getById(Long id);

    Employee getEmpByEmail(String email);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee, Long id);

    void deleteEmpById(Long id);
}


// duplication , null checks, validation and exception handling..., pagination...
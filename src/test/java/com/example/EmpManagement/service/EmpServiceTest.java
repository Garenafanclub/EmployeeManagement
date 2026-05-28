package com.example.EmpManagement.service;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.EmpServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmpServiceTest {

    @Mock
    private EmpRepo empRepo;

    @InjectMocks
    private EmpServiceImp empService;

    @Test
    void createEmployeeTest()
    {
        Employee employee = new Employee();
        empService.createEmployee(employee);
    }
}

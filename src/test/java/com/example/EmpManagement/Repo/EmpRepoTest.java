package com.example.EmpManagement.Repo;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.EmpServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpRepoTest {

    @Mock
    private EmpRepo empRepo;

    @InjectMocks
    private EmpServiceImp empService;

    @BeforeEach
        // runs before EVERY test — fresh data each time
    void setUp() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Rahul");
        testEmployee.setLastName("Sharma");
        testEmployee.setEmail("rahul@test.com");
        testEmployee.setDepartment("Engineering");
        testEmployee.setSalary(70000.0);
    }

    @Test
    void findByEmail()
    {
        Optional<Employee> employee = Optional.of(new Employee());
//      boolean check = employee.isEmpty();
        Employee emp = employee.get();
        emp.setEmail("mayank@gmail.com");
        emp.setDepartment("Java");
        emp.setFirstName("Mayank");
        emp.setLastName("Bansal");
        emp.setSalary(900000.0);
        emp.setId(101L);
        Long id = 101L;

        Mockito.when(empRepo.findById(id)).thenReturn(Optional.of(emp));

        Employee resultEmployee = empService.getById(id);

        Assertions.assertNotNull(resultEmployee);
        Assertions.assertEquals(emp.getId(), resultEmployee.getId());
    }

}

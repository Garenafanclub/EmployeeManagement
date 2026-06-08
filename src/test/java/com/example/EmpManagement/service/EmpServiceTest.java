package com.example.EmpManagement.service;

import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.Imp.EmpServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmpServiceTest {

    @Mock
    private EmpRepo empRepo;

    @InjectMocks
    private EmpServiceImp empService;

    private Employee standardEmployee;
    private Employee existingEmployee;

    @BeforeEach
    void setUp() {
        standardEmployee = new Employee();
        standardEmployee.setId(101L);
        standardEmployee.setFirstName("Rahul");
        standardEmployee.setLastName("Sharma");
        standardEmployee.setEmail("rahul.sharma@gmail.com");
        standardEmployee.setDepartment("Engineering");
        standardEmployee.setSalary(70000.0);

        existingEmployee = new Employee();
        existingEmployee.setId(102L);
        existingEmployee.setFirstName("Mayank");
        existingEmployee.setLastName("Bansal");
        existingEmployee.setEmail("mayank@gmail.com");
        existingEmployee.setDepartment("Java");
        existingEmployee.setSalary(90000.0);
    }

    @Test
    void shouldReturnListOfEmployees_WhenEmployeesExist()
    {
        //Arrange...
        List<Employee> mockedList = List.of(standardEmployee, existingEmployee);

        // Script the Mock to return the mocked list when findAll() is called
        when(empRepo.findAll()).thenReturn(mockedList);

        List<Employee> result = empService.getAllEmp();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Rahul", result.get(0).getFirstName());
        assertEquals("Mayank", result.get(1).getFirstName());
    }

    @Test
    void shouldReturnEmptyList_WhenNoEmployeesExist()
    {
        // ARRANGE..
        when(empRepo.findAll()).thenReturn(List.of());

        // ACT..
        List<Employee> result = empService.getAllEmp();

        // ASSERT...
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmployee_WhenIdExists() {
       // ARRANGE...
        when(empRepo.findById(101L)).thenReturn(Optional.of(standardEmployee));

        // ACT...
        Employee result = empService.getById(101L);

        assertNotNull(result);
        assertEquals(101L, result.getId());
    }

    @Test
    void shouldThrowException_WhenEmployeeIdDoesNotExist() {
          Long nonExistingId = 99L;

          when(empRepo.findById(nonExistingId)).thenReturn(Optional.empty());


        // ACT AND ASSERT...
        RuntimeException exception = assertThrows(RuntimeException.class, ()->
                empService.getById(nonExistingId));

        assertEquals("Emp not found with id: " + nonExistingId, exception.getMessage());
    }

    @Test
    void shouldReturnEmployee_WhenEmailExists() {
        String email = "mayank@gmail.com";

        when(empRepo.findByEmail(email)).thenReturn(Optional.of(existingEmployee));

        Employee result = empService.getEmpByEmail(email);

        assertNotNull(result);
        assertEquals("mayank@gmail.com", result.getEmail());
    }

    @Test
    void shouldThrowException_WhenEmailNotExist()
    {
        String nonExistingEmail = "ab@gmail.com";

        when(empRepo.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->
                empService.getEmpByEmail(nonExistingEmail));

        assertEquals("Emp not found with email: " + nonExistingEmail, exception.getMessage());
    }

    @Test
    void shouldCreateEmployeeSuccessfully_whenEmailIsUnique()
    {
        // 1. ARRANGE  .... DATA PREPARATION....
        // Create the dummy data we will pass into the service...
        Employee newEmployee = new Employee();
        newEmployee.setId(101L);
        newEmployee.setFirstName("Mayank");
        newEmployee.setEmail("abc@gmail.com");

        // Create the dummy data the database SHOULD return after saving the employee...
        Employee savedEmployee = new Employee();
        savedEmployee.setId(101L);
        savedEmployee.setFirstName("Mayank");
        savedEmployee.setEmail("abc@gmail.com");

        when(empRepo.save(any(Employee.class))).thenReturn(savedEmployee);

        // 2. ACT
        // Call the actual method we are testing
        Employee result = empService.createEmployee(newEmployee);

        // 3. ASSERT
        // Verify the result is not null and has the ID assigned by our mock database
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("abc@gmail.com",result.getEmail());
    }

    @Test
    void shouldThrowException_WhenCreatingEmployeeWithExistingEmail()
    {
        // 1. ARRANGE
        Employee newEmployee = new Employee();
        newEmployee.setEmail("abc@gmail.com");

        Employee existingEmployee = new Employee();
        existingEmployee.setEmail("abc@gmail.com");

        when(empRepo.findByEmail("abc@gmail.com")).thenReturn(Optional.of(existingEmployee));

        // 2. ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                empService.createEmployee(newEmployee));

        assertEquals("Emp already exists with email: abc@gmail.com",  exception.getMessage());
    }

    @Test
    void shouldUpdateEmployeeSuccessfully_WhenIdExists()
    {
        // Arrange..
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(101L);
        updatedEmployee.setFirstName("Mayank");
        updatedEmployee.setLastName("Sharma");
        updatedEmployee.setEmail("mayankZkteco@gmail.com");
        updatedEmployee.setSalary(100000.0);

        // find first existing employee by ID and return the standardEmployee as existing data in DB
        when(empRepo.findById(101L)).thenReturn(Optional.of(standardEmployee));

        // when(empRepo.save(standardEmployee)).thenReturn(standardEmployee);
        when(empRepo.save(any(Employee.class))).thenReturn(standardEmployee);

        // ACT....
        Employee result = empService.updateEmployee(updatedEmployee, 101L);

        // Assert..
        assertEquals("Mayank", result.getFirstName());
        assertEquals("Sharma", result.getLastName());

        verify(empRepo, times(1)).findById(101L);
        System.out.println(standardEmployee.getFirstName());
    }

    @Test
    void shouldThrowException_WhenUpdatingNonExistentEmployee()
    {
        // Arrange..
        Employee updatedEmployee = new Employee();
        Long nonExistingId = 99L;

        when(empRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                empService.updateEmployee(updatedEmployee, nonExistingId));

        assertEquals("Emp not found with id: 99",  exception.getMessage());
        System.out.println(standardEmployee.getFirstName());
    }

    @Test
    void shouldDeleteEmployeeSuccessfully_WhenIdExists()
    {
        // Arrange...
        when(empRepo.findById(101L)).thenReturn(Optional.of(standardEmployee));

        empService.deleteEmpById(101L);

        verify(empRepo, times(1)).findById(101L);
        verify(empRepo, times(1)).deleteById(101L);
        System.out.println(standardEmployee.getFirstName());
    }

    @Test
    void shouldThrowException_WhenDeletingNonExistingEmployee()
    {
       when(empRepo.findById(99L)).thenReturn(Optional.empty());

       RuntimeException exception = assertThrows(RuntimeException.class, () ->
               empService.deleteEmpById(99L));

       assertEquals("Emp not found with id: 99", exception.getMessage());
    }
}
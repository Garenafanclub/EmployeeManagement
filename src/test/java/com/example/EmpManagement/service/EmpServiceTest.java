package com.example.EmpManagement.service;

import com.example.EmpManagement.DTOs.EmployeeRequestDTO;
import com.example.EmpManagement.DTOs.EmployeeResponseDTO;
import com.example.EmpManagement.Exceptions.DuplicateResourceException;
import com.example.EmpManagement.Exceptions.ResourceNotFoundException;
import com.example.EmpManagement.Mapper.EmployeeMapper;
import com.example.EmpManagement.Model.Department;
import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.DepRepo;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Repository.UserRepo;
import com.example.EmpManagement.Service.Imp.EmpServiceImp;
import com.example.EmpManagement.Service.Imp.OnboardingNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpServiceTest {

    @Mock private EmpRepo empRepo;
    @Mock private EmployeeMapper employeeMapper;
    @Mock private DepRepo depRepo;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepo userRepo;
    @Mock private OnboardingNotificationService notificationService;

    @InjectMocks
    private EmpServiceImp empService;

    private EmployeeRequestDTO requestDTO;
    private Department department;
    private Employee mappedEmployee;
    private Employee savedEmployee;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = EmployeeRequestDTO.builder()
                .firstName("Mayank")
                .lastName("Kumar")
                .email("mayank.k@company.com")
                .departmentId(1L)
                .salary(50000.0)
                .build();

        department = Department.builder().id(1L).name("Engineering").build();

        mappedEmployee = new Employee();
        mappedEmployee.setFirstName("Mayank");
        mappedEmployee.setLastName("Kumar");
        mappedEmployee.setEmail("mayank.k@company.com");
        mappedEmployee.setSalary(50000.0);

        savedEmployee = new Employee();
        savedEmployee.setId(101L);
        savedEmployee.setFirstName("Mayank");
        savedEmployee.setLastName("Kumar");
        savedEmployee.setEmail("mayank.k@company.com");
        savedEmployee.setSalary(50000.0);
        savedEmployee.setDepartment(department);

        responseDTO = EmployeeResponseDTO.builder()
                .id(101L)
                .firstName("Mayank")
                .lastName("Kumar")
                .email("mayank.k@company.com")
                .build();
    }

    @Test
    void createEmployee_Success_WhenEmailAndDepartmentAreValid() {
        when(empRepo.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(depRepo.findById(1L)).thenReturn(Optional.of(department));
        when(employeeMapper.toEntity(requestDTO)).thenReturn(mappedEmployee);
        when(empRepo.save(mappedEmployee)).thenReturn(savedEmployee);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(employeeMapper.toResponseDTO(savedEmployee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = empService.createEmployee(requestDTO);

        assertNotNull(result);
        assertEquals("mayank.k@company.com", result.getEmail());
        assertNotNull(result.getTemporaryPassword());
        assertEquals(10, result.getTemporaryPassword().length());

        verify(empRepo).save(mappedEmployee);
        verify(userRepo).save(any());
        verify(notificationService).sendWelcomeEmail(eq(savedEmployee), anyString());
    }

    @Test
    void createEmployee_ThrowsDuplicateResourceException_WhenEmailAlreadyExists() {
        when(empRepo.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> empService.createEmployee(requestDTO));

        verify(empRepo, never()).save(any());
        verify(notificationService, never()).sendWelcomeEmail(any(), anyString());
    }

    @Test
    void createEmployee_ThrowsResourceNotFoundException_WhenDepartmentDoesNotExist() {
        when(empRepo.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(depRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empService.createEmployee(requestDTO));

        verify(empRepo, never()).save(any());
    }

    @Test
    void getById_ReturnsEmployee_WhenFound() {
        when(empRepo.findById(101L)).thenReturn(Optional.of(savedEmployee));
        when(employeeMapper.toResponseDTO(savedEmployee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = empService.getById(101L);

        assertEquals("mayank.k@company.com", result.getEmail());
    }

    @Test
    void getById_ThrowsResourceNotFoundException_WhenNotFound() {
        when(empRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empService.getById(999L));
    }

    @Test
    void deleteEmpById_DeletesSuccessfully_WhenEmployeeExists() {
        when(empRepo.findById(101L)).thenReturn(Optional.of(savedEmployee));

        empService.deleteEmpById(101L);

        verify(empRepo).deleteById(101L);
    }

    @Test
    void deleteEmpById_ThrowsResourceNotFoundException_WhenEmployeeDoesNotExist() {
        when(empRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> empService.deleteEmpById(999L));

        verify(empRepo, never()).deleteById(anyLong());
    }

    @Test
    void getAllEmp_ReturnsPagedEmployeeList() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        Page<Employee> employeePage = new PageImpl<>(List.of(savedEmployee), pageable, 1);

        when(empRepo.findAll(any(Pageable.class))).thenReturn(employeePage);
        when(employeeMapper.toResponseDTO(savedEmployee)).thenReturn(responseDTO);

        Page<EmployeeResponseDTO> result = empService.getAllEmp(0, 5, "id", "asc");

        assertEquals(1, result.getTotalElements());
        assertEquals("mayank.k@company.com", result.getContent().get(0).getEmail());
    }
}
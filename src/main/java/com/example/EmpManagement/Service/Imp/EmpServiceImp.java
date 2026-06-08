package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.EmployeeRequestDTO;
import com.example.EmpManagement.DTOs.EmployeeResponseDTO;
import com.example.EmpManagement.Exceptions.DuplicateResourceException;
import com.example.EmpManagement.Exceptions.ResourceNotFoundException;
import com.example.EmpManagement.Mapper.EmployeeMapper;
import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.EmpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class EmpServiceImp implements EmpService {

    private final EmpRepo empRepo;
    private final EmployeeMapper employeeMapper;

    public EmpServiceImp(EmpRepo empRepo, EmployeeMapper employeeMapper)
    {
        this.empRepo = empRepo;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<Employee> getAllEmp() {
        return empRepo.findAll();
    }

    @Override
    public Employee getById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return empRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));
    }

    @Override
    public Employee getEmpByEmail(String email) {
        log.info("Fetching employee with email: {}", email);
        return empRepo.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "email", email));
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        log.info("Attempting to create Employee with email{}", employeeRequestDTO.getEmail());

        /*
            Employee employee = new Employee();
            employee.setFirstName(employeeRequestDTO.getFirstName());
            employee.setLastName(employeeRequestDTO.getLastName());
            employee.setEmail(employeeRequestDTO.getEmail());
            employee.setDepartment(employeeRequestDTO.getDepartment());
            employee.setSalary(employeeRequestDTO.getSalary());

        if(empRepo.existsByEmail(employeeRequestDTO.getEmail()))
        {
            log.warn("Duplicate email attempt: {}", employeeRequestDTO.getEmail());
            throw new DuplicateResourceException("Employee", "email", employeeRequestDTO.getEmail());
        }

        */
        try{
            Employee employee = employeeMapper.toEntity(employeeRequestDTO);
            Employee savedEntity = empRepo.save(employee);
            log.info("Employee created successfully with id: {}", savedEntity.getId());
            return employeeMapper.toResponseDTO(savedEntity);
        }
        catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation - Email is already present: {}", employeeRequestDTO.getEmail());
            throw new DuplicateResourceException("Employee" , "Email", employeeRequestDTO.getEmail());
        }
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Employee employee, Long id) {

            Employee existEmp = empRepo.findById(id)
                    .orElseThrow(()-> new RuntimeException("Emp not found with id: " + id));

            existEmp.setFirstName(employee.getFirstName());
            existEmp.setLastName(employee.getLastName());
            existEmp.setEmail(employee.getEmail());
            existEmp.setDepartment(employee.getDepartment());
            existEmp.setSalary(employee.getSalary());

            try {
                Employee savedEntity = empRepo.save(existEmp);
                return employeeMapper.toResponseDTO(savedEntity);
            }
            catch (DataIntegrityViolationException e) {
                // Admin wants to update an email to one who is already been assigned to someone.
                log.error("Database constraint violation during update - Email is already present: {}", employee.getEmail());
                throw new DuplicateResourceException("Employee" , "Email", employee.getEmail());
            }
    }

    @Override
    public void deleteEmpById(Long id) {
        Employee existEmp = empRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));
        empRepo.deleteById(id);
    }
}

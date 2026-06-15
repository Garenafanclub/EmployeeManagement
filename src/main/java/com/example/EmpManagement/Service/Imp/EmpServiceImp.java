package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.DTOs.EmployeeRequestDTO;
import com.example.EmpManagement.DTOs.EmployeeResponseDTO;
import com.example.EmpManagement.Exceptions.DuplicateResourceException;
import com.example.EmpManagement.Exceptions.ResourceNotFoundException;
import com.example.EmpManagement.Mapper.EmployeeMapper;
import com.example.EmpManagement.Model.Department;
import com.example.EmpManagement.Model.Employee;
import com.example.EmpManagement.Repository.DepRepo;
import com.example.EmpManagement.Repository.EmpRepo;
import com.example.EmpManagement.Service.EmpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class EmpServiceImp implements EmpService {

    private final EmpRepo empRepo;
    private final EmployeeMapper employeeMapper;
    private final DepRepo depRepo;

    public EmpServiceImp(EmpRepo empRepo, EmployeeMapper employeeMapper, DepRepo depRepo)
    {
        this.empRepo = empRepo;
        this.employeeMapper = employeeMapper;
        this.depRepo = depRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmp(int page, int size, String sortBy, String direction) {
        log.info("Fetching employees — page: {}, size: {}, sortBy: {}, direction: {}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch page from DB, map each Employee → EmployeeResponseDTO
        Page<Employee> result = empRepo.findAll(pageable);

        log.info("Fetched page {} of employees. Total elements: {}", page, result.getTotalElements());

        return result.map(employeeMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee savedEmp = empRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));

        return employeeMapper.toResponseDTO(savedEmp);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmpByEmail(String email) {
        log.info("Fetching employee with email: {}", email);
        Employee savedEmp = empRepo.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "email", email));
        return employeeMapper.toResponseDTO(savedEmp);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        log.info("Attempting to create Employee with email{}", employeeRequestDTO.getEmail());

        if(empRepo.existsByEmail(employeeRequestDTO.getEmail()))
        {
            throw new DuplicateResourceException("Employee", "Email", employeeRequestDTO.getEmail());
        }

        // Fetch the Department using the ID sent by the frontend
        Department department = depRepo.findById(employeeRequestDTO.getDepartmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Department", "id", employeeRequestDTO.getDepartmentId()));

        // Map the DTO TO ENTITY
        Employee employee = employeeMapper.toEntity(employeeRequestDTO);

        // ATTACH THE REAL DEPARTMENT ENTITY...
        employee.setDepartment(department);

        Employee savedEntity = empRepo.save(employee);
        log.info("Employee created successfully with id: {}", savedEntity.getId());

        return employeeMapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(EmployeeRequestDTO requestDTO, Long id) {

            Employee existEmp = empRepo.findById(id)
                    .orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));

            Department department = depRepo.findById(requestDTO.getDepartmentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Deparment", "id", requestDTO.getDepartmentId()));

            // Mapping from DTO --- ENTITY
            employeeMapper.updateEntityFromDto(requestDTO, existEmp);
            existEmp.setDepartment(department);

        Employee savedEntity = empRepo.save(existEmp);
        return employeeMapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional
    public void deleteEmpById(Long id) {
        Employee existEmp = empRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));
        empRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> searchEmployeeByName(String prefix, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> result = empRepo.searchByFirstName(prefix, pageable);
        return result.map(employeeMapper::toResponseDTO);
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployeeByDepartment(Long departmentId, int pageNumber, int pageSize, String sortBy, String direction) {
        log.info("Fetching employees by departmentId: {} — page: {}, size: {}, sortBy: {}, direction: {}",
                departmentId, pageNumber, pageSize, sortBy, direction);

        // 1. Verify if department exists first to throw your precise custom ResourceNotFoundException
        if (!depRepo.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        // 2. Setup the precise sorting direction
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // 3. Request the precise subset page slice from the database
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Employee> result = empRepo.findByDepartmentId(departmentId, pageable);

        log.info("Fetched page {} for department {}. Total items: {}", pageNumber, departmentId, result.getTotalElements());
        return result.map(employeeMapper::toResponseDTO);
    }
}

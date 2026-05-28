package com.example.EmpManagement.Repository;

import com.example.EmpManagement.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}

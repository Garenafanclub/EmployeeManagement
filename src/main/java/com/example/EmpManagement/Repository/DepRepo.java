package com.example.EmpManagement.Repository;

import com.example.EmpManagement.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepRepo extends JpaRepository<Department, Long> {
}

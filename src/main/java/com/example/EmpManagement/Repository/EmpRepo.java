package com.example.EmpManagement.Repository;

import com.example.EmpManagement.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);


    Page<Employee> findAll(Pageable pageable);

    // The core idea is simple: instead of SELECT * FROM employee which loads all 50 (or 50,000) rows,
    // you always add LIMIT and OFFSET.
    // LIMIT = how many records per page.
    // OFFSET = how many to skip.
    // SELECT * FROM employee ORDER BY id ASC LIMIT 15 ← your page size OFFSET 0 ← page 1 × size 15 = skip 0 rows
}

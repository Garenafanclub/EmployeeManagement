package com.example.EmpManagement.Repository;

import com.example.EmpManagement.Model.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpRepo extends JpaRepository<Employee, Long> {

    // CrudRepository --- ListCrudRepository --- PagingAndSortingRepository --- ListPagingAndSortingRepository...

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "Select e from Employee e LEFT JOIN FETCH e.pfAccount LEFT JOIN FETCH e.department",
              countQuery = "select count(e) from Employee e")
    @SuppressWarnings("NullableProblems")
    Page<Employee> findAll(@NonNull Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE :prefix%")
    Page<Employee> searchByFirstName(String prefix, Pageable pageable);


    // The core idea is simple: instead of SELECT * FROM employee which loads all 50 (or 50,000) rows,
    // you always add LIMIT and OFFSET.
    // LIMIT = how many records per page.
    // OFFSET = how many to skip.
    // SELECT * FROM employee ORDER BY id ASC LIMIT 15 ← your page size OFFSET 0 ← page 1 × size 15 = skip 0 rows
}

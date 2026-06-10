package com.example.EmpManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    // THIS IS THE FIX: Tell PostgreSQL this column must be strictly unique!
    @Column(unique = true, nullable = false)

    private String email;
    private Double salary;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id" , nullable = false)
    private Department department;

}

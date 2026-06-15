package com.example.EmpManagement.Model;

import jakarta.persistence.*;
import lombok.*;

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

    // Tell PostgreSQL this column must be strictly unique!
    @Column(unique = true, nullable = false)
    private String email;

    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_id" , nullable = false)
    private Department department;

    @OneToOne(mappedBy = "employee",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE} , orphanRemoval = true
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PfAccount pfAccount;

}

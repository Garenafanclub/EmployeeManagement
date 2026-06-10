package com.example.EmpManagement.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;


    @OneToMany(mappedBy = "department",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},  //  ← safer than ALL
            fetch = FetchType.LAZY)
    @ToString.Exclude          // ← prevent toString() infinite loop
    @EqualsAndHashCode.Exclude   // ← prevent equals/hashCode infinite loop
    @Builder.Default           // ← make @Builder respect the initialization
    private List<Employee> employees = new ArrayList<>();
}


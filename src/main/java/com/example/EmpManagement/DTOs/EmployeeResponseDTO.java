package com.example.EmpManagement.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    // WHEN WE SEND THE EMPLOYEE TO THE FRONTEND IT SENDED WITH NESTED DEPARTMENT...
    private DepartmentResponseDTO department;

    private PFResponseDTO pfAccount;
}

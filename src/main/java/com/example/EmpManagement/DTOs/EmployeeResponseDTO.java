package com.example.EmpManagement.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponseDTO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String temporaryPassword;

    // WHEN WE SEND THE EMPLOYEE TO THE FRONTEND IT SENDED WITH NESTED DEPARTMENT...
    private DepartmentResponseDTO department;

    private PFResponseDTO pfAccount;
}

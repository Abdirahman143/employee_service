package com.ems.employee_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String employeeId;
    private String email;
    private String position;
    private String phoneNumber;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String status;
}

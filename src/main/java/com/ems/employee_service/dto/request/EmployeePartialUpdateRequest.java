package com.ems.employee_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePartialUpdateRequest {
    private String email;
    private BigDecimal salary;

    // Getters and setters
}

package com.ems.employee_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_tbl",
uniqueConstraints = @UniqueConstraint(columnNames = "email",name = "uniqueEmailConstraint")
)
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String employeeId;
    @NotBlank(message ="Name is required!")
    private String name;
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format")
    @Column(unique = true)
    private String email;
    private String position;
    private String phoneNumber;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String status;

    
}

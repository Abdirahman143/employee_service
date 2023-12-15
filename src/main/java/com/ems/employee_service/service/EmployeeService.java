package com.ems.employee_service.service;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {
    ResponseEntity<Employee> addEmployee(EmployeeRequest employeeRequest);
}

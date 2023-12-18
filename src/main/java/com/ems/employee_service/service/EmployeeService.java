package com.ems.employee_service.service;

import com.ems.employee_service.customException.UserNotFoundException;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    ResponseEntity<Employee> addEmployee(EmployeeRequest employeeRequest);

    //get all the employee
    ResponseEntity<List<EmployeeResponse>>getAllEmployee();

    //get employeeByEmployeeID
    Optional<EmployeeResponse> getEmployeeById(String empId) throws UserNotFoundException;
}

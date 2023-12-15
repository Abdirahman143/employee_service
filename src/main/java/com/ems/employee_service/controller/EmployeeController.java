package com.ems.employee_service.controller;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

   private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    ResponseEntity<Employee>addEmployee(@RequestBody EmployeeRequest employeeRequest){
       return employeeService.addEmployee(employeeRequest);
    }
}

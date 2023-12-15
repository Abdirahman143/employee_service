package com.ems.employee_service.service;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //creat employee

    @Override
    public ResponseEntity<Employee>addEmployee(EmployeeRequest employeeRequest){
        LocalDate currentDate = LocalDate.now();
        Employee employee = Employee.
                builder().
                employeeId(employeeRequest.getEmployeeId()).
                name(employeeRequest.getName()).
                email(employeeRequest.getEmail()).
                position(employeeRequest.getPosition()).
                phoneNumber(employeeRequest.getPhoneNumber()).
                salary(employeeRequest.getSalary()).
                hireDate(currentDate).
                status(employeeRequest.getStatus()).
                build();
        return new ResponseEntity<>(employeeRepository.save(employee), HttpStatus.CREATED);
    }


}

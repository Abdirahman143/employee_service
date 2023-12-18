package com.ems.employee_service.service;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest validEmployeeRequest;
    private Employee expectedEmployee;

    @BeforeEach
    void setUp() {
        // Initialize some common objects used in multiple tests
        validEmployeeRequest = new EmployeeRequest();
        validEmployeeRequest.setEmployeeId("E12356");
        validEmployeeRequest.setName("Abdirahman");
        validEmployeeRequest.setEmail("abdirahman.bashir@gmail.com");
        validEmployeeRequest.setPosition("Designer");
        validEmployeeRequest.setSalary(BigDecimal.valueOf(89000));
        validEmployeeRequest.setPhoneNumber("123-3455-6789");
        validEmployeeRequest.setStatus("Active");

// Populate expectedEmployee with data corresponding to validEmployeeRequest
        expectedEmployee = new Employee();
        expectedEmployee.setEmployeeId("E12356");
        expectedEmployee.setName("Abdirahman");
        expectedEmployee.setEmail("abdirahman.bashir@gmail.com");
        expectedEmployee.setPosition("Designer");
        expectedEmployee.setSalary(BigDecimal.valueOf(89000));
        expectedEmployee.setPhoneNumber("123-3455-6789");
        expectedEmployee.setStatus("Active");
        expectedEmployee.setHireDate(LocalDate.now());

        // Setup common mocking behavior for success scenario
        when(employeeRepository.save(any(Employee.class))).thenReturn(expectedEmployee);
    }

    // Test for successful employee addition
    @Test
    void addEmployeeSuccessTest() {
        ResponseEntity<Employee> response = employeeService.addEmployee(validEmployeeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Employee responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(validEmployeeRequest.getEmployeeId(), responseBody.getEmployeeId());
        assertEquals(validEmployeeRequest.getName(), responseBody.getName());
        assertEquals(validEmployeeRequest.getEmail(), responseBody.getEmail());
        assertEquals(validEmployeeRequest.getPosition(), responseBody.getPosition());
        assertEquals(validEmployeeRequest.getPhoneNumber(), responseBody.getPhoneNumber());
        assertEquals(validEmployeeRequest.getStatus(), responseBody.getStatus());

        // additional field assertions

        // Verifying repository interactions
        verify(employeeRepository).save(any(Employee.class));
    }


    // Test for handling invalid data
    @Test
    void addEmployeeWithInvalidDataTest() {
        EmployeeRequest invalidEmployeeRequest = new EmployeeRequest();
        // Populate with invalid data

        // Assuming service throws a custom exception for invalid data
        // Mock the behavior here if necessary

        // Execute and expect a custom exception or specific behavior
        // Use assertThrows(...) if an exception is expected
    }

    // Test for handling a scenario like employee not found, or any other exception
    @Test
    void addEmployeeErrorScenarioTest() {
        EmployeeRequest employeeRequestCausingError = new EmployeeRequest();
        // Populate with data that would lead to an error scenario

        // Mock an exception scenario
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            employeeService.addEmployee(employeeRequestCausingError);
        });
    }

    // Additional tests for other business logic, edge cases, etc.
}
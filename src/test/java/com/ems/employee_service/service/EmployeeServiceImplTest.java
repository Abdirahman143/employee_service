package com.ems.employee_service.service;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    private Employee validEmployee;
    private EmployeeRequest employeeRequest;
    static private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImplTest.class);

    @BeforeEach
    void setUp() {
        validEmployee = new Employee();


        //employee Request initiation

        employeeRequest = EmployeeRequest.
                builder().
                employeeId("E123490").
                email("bashir.abdi@test.com").
                name("Bashir").
                position("Developer").
                phoneNumber("+2547-0000-00").
                salary(new BigDecimal("980000")).
                status("Active").
                build();
        // Using BeanUtils.copyProperties to copy properties from EmployeeDto(EmployeeRequest) to Employee for the test.
        BeanUtils.copyProperties(employeeRequest, validEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);
    }

    @Test
    void addEmployeeSuccessTest() {
        ResponseEntity<Employee> response = employeeService.addEmployee(employeeRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

//       Employee responseBody = response.getBody();
//       assertNotNull(responseBody);
//       assertEquals(employeeRequest.getEmployeeId(),responseBody.getEmployeeId());
//       assertEquals(employeeRequest.getEmail(), responseBody.getEmail());
//       assertEquals(employeeRequest.getStatus(), responseBody.getStatus());
//        assertThat(responseBody).isNotNull();
//        assertThat(responseBody.getEmployeeId()).isEqualTo(employeeRequest.getEmployeeId());
//        verify(employeeRepository).save(any(Employee.class));


        //using assertJ to chain all the assertion

        Employee responseBody = response.getBody();
        logger.info("response{}", responseBody);
        assertThat(responseBody).
                as("Check that the response body is not null and its fields match the expected values").
                isNotNull().
                satisfies(employee -> {
                    assertThat(employee.getEmployeeId()).as("Check Employee ID").
                            withFailMessage("Expected employeeId to be %s but was %s", employeeRequest.getEmployeeId(), employee.getEmployeeId()).
                            isEqualTo(employeeRequest.getEmployeeId());
                    assertThat(employee.getEmail()).as("Check employee Email").
                            isEqualTo(employeeRequest.getEmail());
                    assertThat(employee.getName()).as("Check employee Name:").isEqualTo(employeeRequest.getName());

                });


// Verify that the save method was called exactly once with any Employee object
        verify(employeeRepository).save(any(Employee.class));


    }

    @AfterEach
    void tearDown() {
        // If you've used any mock objects, you might want to reset them
        Mockito.reset(employeeRepository);

        // If you inserted data into an in-memory database, you might want to remove it
        //employeeRepository.deleteAll();

        // Nullify the objects to ensure they're garbage collected
        validEmployee = null;
        employeeRequest = null;
    }


}
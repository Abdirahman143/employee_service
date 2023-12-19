package com.ems.employee_service.controller;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private EmployeeRequest employeeRequest;
    private Employee employee;

    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper  = new ObjectMapper();
        // Populating EmployeeRequest with test data
        if(employeeRequest==null) {
            employeeRequest = new EmployeeRequest();
            employeeRequest.setEmployeeId("E12345");
            employeeRequest.setName("John Doe");
            employeeRequest.setEmail("john.doe@example.com");
            employeeRequest.setPosition("Developer");
            employeeRequest.setPhoneNumber("123-456-7890");
            employeeRequest.setSalary(new BigDecimal("75000.00"));
            employeeRequest.setStatus("Active");
        }
// Populating Employee with expected result
        if(employee==null) {
            employee = new Employee();
            employee.setEmployeeId("E12345");
            employee.setName("John Doe");
            employee.setEmail("john.doe@example.com");
            employee.setPosition("Developer");
            employee.setPhoneNumber("123-456-7890");
            employee.setSalary(new BigDecimal("75000.00"));
            employee.setHireDate(LocalDate.now());
            employee.setStatus("Active");
        }
    }

    @Test
    void addEmployeeTest() throws Exception {
        String validRequestJson = objectMapper.writeValueAsString(employeeRequest);
        when(employeeService.addEmployee(any(EmployeeRequest.class))).thenReturn(ResponseEntity.ok(employee));
        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(employee.getEmployeeId())) // Example of checking response content
                .andDo(print());
        verify(employeeService).addEmployee(any(EmployeeRequest.class));
    }

    @Test
    void addEmployeeWithEmptyFieldsTest() throws Exception {
        String emptyFieldsJson = "{"
                + "\"employeeId\": \"\","
                + "\"email\": \"\","
                + "\"name\": \"\","
                + "\"position\": \"SWE\","
                + "\"phoneNumber\": \"123-3456-980\","
                + "\"salary\": 100000,"
                + "\"status\": \"InActive\""
                + "}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyFieldsJson))
                .andExpect(status().isBadRequest())
            //   .andExpect(jsonPath("$.message").value("Validation Errors"))
//                .andExpect(jsonPath("$.errors").isArray())
//                .andExpect(jsonPath("$.errors", hasSize(3)))
//                .andExpect(jsonPath("$.errors[0]").value("employeeRequest : Name is required"))
//                .andExpect(jsonPath("$.errors[1]").value("employeeRequest : Email is required"))
//                .andExpect(jsonPath("$.errors[2]").value("employeeRequest : Employee ID is required"))
                .andDo(print());

        verify(employeeService, never()).addEmployee(any(EmployeeRequest.class));
    }






}
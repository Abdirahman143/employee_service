package com.ems.employee_service.controller;


import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void addEmployeeTest() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(); // populate with test data
        Employee employee = new Employee(); // populate with expected result

        when(employeeService.addEmployee(any(EmployeeRequest.class))).thenReturn(ResponseEntity.ok(employee));

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("    {\n" +
                                "        \"employeeId\": \"E12345\",\n" +
                                "        \"email\": \"john.doe@example.com\",\n" +
                                "        \"name\": \"John Doe\",\n" +
                                "        \"position\": \"Developer\",\n" +
                                "        \"phoneNumber\": \"123-456-7890\",\n" +
                                "        \"salary\": 75000.00,\n" +
                                "        \"hireDate\": \"2023-12-15\",\n" +
                                "        \"status\": \"Active\"\n" +
                                "    }"))
                .andExpect(status().isOk());
    }

    @Test
    void addEmployeeWithInvalidDataTest() throws Exception {
        String invalidRequestJson = "{"
                + "\"employeeId\": \"\","
                + "\"email\": \"invalid-email\","
                + "\"name\": \"\","
                + "\"position\": \"Developer\","
                + "\"phoneNumber\": \"123-456-7890\","
                + "\"salary\": -1000.00,"
                + "\"status\": \"Active\""
                + "}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson))
                .andExpect(status().isBadRequest());
    }

}
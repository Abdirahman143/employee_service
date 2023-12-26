package com.ems.employee_service.controller;

import com.ems.employee_service.customException.CustomizedExceptionHandler;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
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
        // Initialize your CustomizedExceptionHandler
        CustomizedExceptionHandler customizedExceptionHandler = new CustomizedExceptionHandler();

        // Setup MockMvc to include the ControllerAdvice
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(customizedExceptionHandler) // Register CustomizedExceptionHandler
                .build();

        objectMapper = new ObjectMapper();

        employeeRequest = createTestEmployeeRequest();
        employee = createTestEmployee();
    }

    private EmployeeRequest createTestEmployeeRequest() {
        return EmployeeRequest.builder()
                .employeeId("E12345")
                .name("John Doe")
                .email("john.doe@example.com")
                .position("Developer")
                .phoneNumber("123-456-7890")
                .salary(new BigDecimal("75000.00"))
                .status("Active")
                .build();
    }

    private Employee createTestEmployee() {
        return Employee.builder()
                .employeeId("E12345")
                .name("John Doe")
                .email("john.doe@example.com")
                .position("Developer")
                .phoneNumber("123-456-7890")
                .salary(new BigDecimal("75000.00"))
                .hireDate(LocalDate.now())
                .status("Active")
                .build();
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
    void addEmployeeWithEmptyFieldsTest() throws RuntimeException,Exception {
        //arrange
        //arrange
        EmployeeRequest emptyRequest = new EmployeeRequest();
        emptyRequest.setEmployeeId("");
        emptyRequest.setEmail("");
        emptyRequest.setName("");
        emptyRequest.setSalary(new BigDecimal("89000"));
        emptyRequest.setPosition("Developer");
        emptyRequest.setPhoneNumber("+2547-0034-4532");
        emptyRequest.setStatus("Active");
        String invalidJsonRequest = objectMapper.writeValueAsString(emptyRequest);



        //act and assert

      mockMvc.perform(post("/api/v1/employee").
              contentType(MediaType.APPLICATION_JSON).
              content(invalidJsonRequest)).
              andDo(print()).
              andExpect(status().isBadRequest()).
              andExpect(jsonPath("$.message").value("Validation Errors")).
              andExpect(jsonPath("$.errors").isArray()).
              andExpect(jsonPath("$.errors",hasSize(3))).
              andExpect(jsonPath("$.errors[0]").value("employeeRequest : Name is required")).
              andExpect(jsonPath("$.errors[1]").value("employeeRequest : Email is required")).
              andExpect(jsonPath("$.errors[2]").value("employeeRequest : Employee ID is required")).
              andDo(print());


      //verify
        verify(employeeService,never()).addEmployee(any(EmployeeRequest.class));

    }


    @Test
    void addEmployeeWithDuplicateEmployeeIDAndEmail() throws Exception {
        //arrange
        EmployeeRequest duplicateRequest = new EmployeeRequest();
        duplicateRequest.setEmployeeId("E12345");
        duplicateRequest.setName("John Doe");
        duplicateRequest.setEmail("john.doe@example.com");
        duplicateRequest.setPosition("Developer");
        duplicateRequest.setPhoneNumber("123-456-7890");
        duplicateRequest.setSalary(new BigDecimal("75000.00"));
        duplicateRequest.setStatus("Active");

        //converting to string
        String validEmployeeRequestJson = objectMapper.writeValueAsString(employeeRequest);
        String duplicateJsonRequest = objectMapper.writeValueAsString(duplicateRequest);

        // Act & Assert for the first valid request
        when(employeeService.addEmployee(any(EmployeeRequest.class))).thenReturn(ResponseEntity.ok(employee));
        mockMvc.perform(post("/api/v1/employee").
        contentType(MediaType.APPLICATION_JSON).
        content(validEmployeeRequestJson)).
                andExpect(status().isOk()).
                andDo(print());

        // Verify the interaction for the first valid request
        verify(employeeService).addEmployee(any(EmployeeRequest.class));


        //adding duplicate
        // Simulate a duplicate entry scenario

        when(employeeService.addEmployee(any(EmployeeRequest.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry for employeeId or email"));

        // Act & Assert for the duplicate request

        mockMvc.perform(post("/api/v1/employee").
                contentType(MediaType.APPLICATION_JSON).
                content(duplicateJsonRequest)).
                andExpect(jsonPath("$.message").value("Data Integrity Error")).
                andExpect(jsonPath("$.errors[0]").value("Operation cannot be performed due to a data integrity violation.")).
                andDo(print());


        // Verify that the service method was indeed called with the duplicate request
        verify(employeeService, times(2)).addEmployee(any(EmployeeRequest.class));


    }




}
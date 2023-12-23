package com.ems.employee_service.service;

import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;

import com.ems.employee_service.service.mapper.EmployeeMapper;

import org.assertj.core.api.Condition;



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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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

    private EmployeeResponse expectedEmployeeResponse;
    @Mock
    private EmployeeMapper employeeMapper;


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

        // Setup test data directly
        validEmployee = Employee.builder()
                .employeeId("E123490")
                .email("bashir.abdi@test.com")
                .name("Bashir")
                .position("Developer")
                .phoneNumber("+2547-0000-00")
                .salary(new BigDecimal("980000"))
                .status("Active")
                .build();

        expectedEmployeeResponse = EmployeeResponse.builder()
                .employeeId("E123490")
                .email("bashir.abdi@test.com")
                .name("Bashir")
                .position("Developer")
                .phoneNumber("+2547-0000-00")
                .salary(new BigDecimal("980000"))
//                .hireDate(LocalDate.now())
                .status("Active")
                .build();



        // Using BeanUtils.copyProperties to copy properties from EmployeeDto(EmployeeRequest) to Employee for the test.
        //  BeanUtils.copyProperties(employeeRequest, validEmployee);


        // Using BeanUtils.copyProperties to copy properties from EmployeeDto(EmployeeRequest) to Employee for the test.
        BeanUtils.copyProperties(employeeRequest, validEmployee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);

    }

    @Test
    void addEmployeeSuccessTest() {

        when(employeeRepository.save(any(Employee.class))).thenReturn(validEmployee);


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


        //using assertJ to chain all the assertion


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

    @Test
    void findAll_should_return_employee_list() {
        List<Employee>mockEmployee = List.of(
                Employee.builder()
                        .employeeId("E123491")
                        .email("ahmed.abdi@test.com")
                        .name("Ahmed")
                        .position("Developer")
                        .phoneNumber("+2547-0000-00")
                        .salary(new BigDecimal("70000"))
                        .status("Active")
                        .build(),
                Employee.builder()
                        .employeeId("E123492")
                        .email("bashir.abdi@test.com")
                        .name("Bashir")
                        .position("Developer")
                        .phoneNumber("+2547-0000-00")
                        .salary(new BigDecimal("80000"))
                        .status("Active")
                        .build(),
                Employee.builder()
                        .employeeId("E123495")
                        .email("fatima.abdi@test.com")
                        .name("Fatima")
                        .position("QA")
                        .phoneNumber("+2547-0040-00")
                        .salary(new BigDecimal("90000"))
                        .status("Active")
                        .build()
        );

        //arrange

        when(employeeRepository.findAll()).thenReturn(mockEmployee);


        //act
        ResponseEntity<List<EmployeeResponse>>response =employeeService.getAllEmployee();

        //assert

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockEmployee.size(), response.getBody().size());


        // Verify interactions
        verify(employeeRepository).findAll();

    }


    //get employee by Id
    @Test
    void find_employee_by_id_should_return_success(){
        //arrange

        when(employeeRepository.findEmployeesByEmployeeId(validEmployee.getEmployeeId())).thenReturn(Optional.of(validEmployee));


        //act
        Optional<EmployeeResponse>response = employeeService.getEmployeeById(validEmployee.getEmployeeId());

        //assert

        assertEquals(expectedEmployeeResponse, response.get());
        assertTrue(response.isPresent());

        // Advanced assertions

       response.ifPresent(employee -> {
            assertThat(employee).isEqualToComparingFieldByField(expectedEmployeeResponse);
           // Numeric Assertions
           assertThat(employee.getSalary()).as("Salary")
                   .isNotNull()
                   .isPositive()
                   .isEqualByComparingTo(new BigDecimal("980000")); // Use isEqualByComparingTo for BigDecimal comparisons

           // Date Assertions - if you have date fields
//            assertThat(employee.getHireDate()).as("Hire Date")
//            .isBeforeOrEqualTo(LocalDate.now());

           // String Assertions - for more detailed string checks
           assertThat(employee.getEmail()).as("Email format")
                   .contains("@")
                   .endsWith(".com");
           // Custom Condition - for domain-specific rules or complex conditions
           Condition<EmployeeResponse> activeStatusCondition = new Condition<>(
                   emp -> "Active".equals(emp.getStatus()),
                   "Employee is in active status"
           );
           assertThat(employee).as("Check active status").has(activeStatusCondition);

        response.ifPresent(employee -> {
            assertThat(employee).isEqualToComparingFieldByField(expectedEmployeeResponse);
            // Numeric Assertions
            assertThat(employee.getSalary()).as("Salary")
                    .isNotNull()
                    .isPositive()
                    .isEqualByComparingTo(new BigDecimal("980000")); // Use isEqualByComparingTo for BigDecimal comparisons

            // Date Assertions - if you have date fields
//            assertThat(employee.getHireDate()).as("Hire Date")
//            .isBeforeOrEqualTo(LocalDate.now());

            // String Assertions - for more detailed string checks
            assertThat(employee.getEmail()).as("Email format")
                    .contains("@")
                    .endsWith(".com");
            // Custom Condition - for domain-specific rules or complex conditions
            Condition<EmployeeResponse> activeStatusCondition = new Condition<>(
                    emp -> "Active".equals(emp.getStatus()),
                    "Employee is in active status"
            );
            assertThat(employee).as("Check active status").has(activeStatusCondition);


        });
        //verify Interaction

        verify(employeeRepository).findEmployeesByEmployeeId(validEmployee.getEmployeeId());
    }


    }







}




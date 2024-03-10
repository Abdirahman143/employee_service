package com.ems.employee_service.controller;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;
import com.ems.employee_service.utils.TestContainerManager;
import com.ems.employee_service.utils.TestDataGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.internal.CoreLogging.logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeTestContainerControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeTestContainerControllerTest.class);

    @BeforeEach
    void setUp() {
        TestContainerManager.startContainer(); // Start the PostgreSQL container before each test
    }

    @AfterEach
    void tearDown() {
        TestContainerManager.stopContainer(); // Stop the PostgreSQL container after each test
    }

    @Test
    @Order(1)
    @DisplayName("Verify Add employee should return success and persist data in the database")
    void addEmployeeTest() throws Exception {
        // Set base URL for the WebClient
        String baseUrl = "http://localhost:" + port;
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        // Given
        EmployeeRequest employeeRequest = createTestEmployeeRequest();
        employeeRequest.setEmail(TestDataGenerator.generateUniqueEmail(employeeRequest.getName()));
        employeeRequest.setEmployeeId(TestDataGenerator.generateUniqueEmployeeId());

        // When
        Mono<Employee> result = webClient
                .post()
                .uri("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employeeRequest))
                .retrieve()
                .bodyToMono(Employee.class);

        // Then
        Employee employee = result.block(); // Block until the result is available
        assertThat(employee).isNotNull();

        // Query the database to check if the employee data is persisted
        Optional<Employee> optionalEmployee = employeeRepository.findEmployeesByEmployeeId(employeeRequest.getEmployeeId());

        logger.info("Employee Details:"+optionalEmployee);

        // Validate if the employee data exists in the database
        assertThat(optionalEmployee).isPresent();
        Employee persistedEmployee = optionalEmployee.get();

        // Validate if the attributes of the employee record match the posted data
        assertThat(persistedEmployee.getName()).isEqualTo(employeeRequest.getName());
        assertThat(persistedEmployee.getEmail()).isEqualTo(employeeRequest.getEmail());
        assertThat(persistedEmployee.getPosition()).isEqualTo(employeeRequest.getPosition());
        assertThat(persistedEmployee.getPhoneNumber()).isEqualTo(employeeRequest.getPhoneNumber());
        assertThat(persistedEmployee.getSalary()).isEqualByComparingTo(employeeRequest.getSalary());
        assertThat(persistedEmployee.getStatus()).isEqualTo(employeeRequest.getStatus());
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
}
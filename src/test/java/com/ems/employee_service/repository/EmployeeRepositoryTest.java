package com.ems.employee_service.repository;

import com.ems.employee_service.entity.Employee;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {
    static private final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryTest.class);
    @Autowired
    EmployeeRepository employeeRepository;
Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.
                builder().
                employeeId("E23456").
                email("abdirahman.ali66@test.com").
                name("Bashir").
                position("SWE").
                salary(new BigDecimal("560000.00")).
                phoneNumber("+2547-9900-6533").
                hireDate(LocalDate.now()).
                status("Active").
                build();
        employeeRepository.save(employee);
    }

    @AfterEach
    void tearDown() {
       employee = null;
       employeeRepository.deleteAll();;
    }

    @Test
    void testFindEmployeesByEmployeeId_Found(){
        Optional<Employee> foundEmployee = employeeRepository.findEmployeesByEmployeeId(employee.getEmployeeId());
        assertTrue(foundEmployee.isPresent(), "Employee should be found");
        assertEquals(employee.getEmployeeId(),foundEmployee.get().getEmployeeId(),"Employee Id should match");
        assertEquals(employee.getHireDate(),foundEmployee.get().getHireDate(),"Hire date should match");
    }

    @Test
    void testFindEmployeesByEmployeeId_NotFound(){
        Optional<Employee>NotFoundEmployee = employeeRepository.findEmployeesByEmployeeId("E12342");
        logger.info("checking if it is getting to here");
        assertFalse(NotFoundEmployee.isPresent(),"Employee should not be found");
        assertTrue(NotFoundEmployee.isEmpty());

    }
}

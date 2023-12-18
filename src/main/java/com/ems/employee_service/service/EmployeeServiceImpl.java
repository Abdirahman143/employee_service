package com.ems.employee_service.service;

import com.ems.employee_service.customException.UserNotFoundException;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.repository.EmployeeRepository;
import com.ems.employee_service.service.mapper.EmployeeMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    //get all the employee
@Override
public ResponseEntity<List<EmployeeResponse>>getAllEmployee(){
        List<Employee> employeeList= employeeRepository.findAll();
        return  new ResponseEntity<>(
                employeeList.stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeResponse).
                        collect(Collectors.toList()),
                HttpStatus.FOUND
        );
    }
//get employeeByEmployeeID
@Override
public Optional<EmployeeResponse> getEmployeeById(String empId) throws UserNotFoundException {
    Optional<Employee> employeeOptional = employeeRepository.findEmployeesByEmployeeId(empId);

    if (!employeeOptional.isPresent()) {
        throw new UserNotFoundException("Employee ID " + empId + " not found. Please try with a valid ID.");
    }

    Employee employee = employeeOptional.get();
    EmployeeResponse employeeResponse = EmployeeResponse.builder()
            .employeeId(employee.getEmployeeId())
            .name(employee.getName())
            .email(employee.getEmail())
            .position(employee.getPosition())
            .phoneNumber(employee.getPhoneNumber())
            .salary(employee.getSalary())
            .hireDate(employee.getHireDate())
            .status(employee.getStatus())
            .build();

    return Optional.of(employeeResponse);
}




}

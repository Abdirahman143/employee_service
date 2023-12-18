package com.ems.employee_service.controller;

import com.ems.employee_service.customException.UserNotFoundException;
import com.ems.employee_service.dto.request.EmployeePartialUpdateRequest;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

   private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    ResponseEntity<Employee>addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest){
       return employeeService.addEmployee(employeeRequest);
    }


    //get all Employee
    @GetMapping()
    ResponseEntity<List<EmployeeResponse>>getAllEmployee(){
        return  employeeService.getAllEmployee();
    }

    @GetMapping("/{employeeId}")
    Optional<EmployeeResponse>getEmployeeByEmployeeId(@PathVariable(value = "employeeId")
                                                      String employeeId) throws UserNotFoundException {
        return  employeeService.getEmployeeById(employeeId);
    }

    @PutMapping("/{employeeId}")
    ResponseEntity<EmployeeResponse>updateEmployee(@RequestBody  EmployeeRequest employeeRequest,
                                           @PathVariable(value = "employeeId") String employeeId) throws UserNotFoundException {
        return  employeeService.updateEmployee(employeeRequest,employeeId);
    }


    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployeePartial(@RequestBody EmployeePartialUpdateRequest updateRequest,
                                                                  @PathVariable("employeeId") String employeeId) throws UserNotFoundException {
        return employeeService.updateEmployeePartial(updateRequest, employeeId);
    }

}

package com.ems.employee_service.controller;

import com.ems.employee_service.customException.UserNotFoundException;
import com.ems.employee_service.dto.request.EmployeePartialUpdateRequest;
import com.ems.employee_service.dto.request.EmployeeRequest;
import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import com.ems.employee_service.service.EmployeeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

   private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //Add employee
    @PostMapping()
    @Operation(summary = "Add a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    ResponseEntity<Employee>addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest){
       return employeeService.addEmployee(employeeRequest);
    }


    //get all Employee
    @GetMapping()
    ResponseEntity<List<EmployeeResponse>>getAllEmployee(){
        return  employeeService.getAllEmployee();
    }

    //checking for content negotiation api versioning
    @GetMapping(produces = "application/vnd.cbc.app-v2+json")
    ResponseEntity<List<EmployeeResponse>>getAllEmployeeV2(){
        return  employeeService.getAllEmployee();
    }
    @GetMapping("/{employeeId}")
    Optional<EmployeeResponse>getEmployeeByEmployeeId(@PathVariable(value = "employeeId")
                                                      String employeeId) throws UserNotFoundException {
        return  employeeService.getEmployeeById(employeeId);
    }

    @PutMapping("/{employeeId}")
    ResponseEntity<EmployeeResponse>updateEmployee(@Valid @RequestBody  EmployeeRequest employeeRequest,
                                           @PathVariable(value = "employeeId") String employeeId) throws UserNotFoundException {
        return  employeeService.updateEmployee(employeeRequest,employeeId);
    }


    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployeePartial(@Valid @RequestBody EmployeePartialUpdateRequest updateRequest,
                                                                  @PathVariable("employeeId") String employeeId) throws UserNotFoundException {
        return employeeService.updateEmployeePartial(updateRequest, employeeId);
    }

}

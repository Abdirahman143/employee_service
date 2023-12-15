package com.ems.employee_service.service.mapper;

import com.ems.employee_service.dto.response.EmployeeResponse;
import com.ems.employee_service.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "employeeId", target = "employeeId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "position", target = "position")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "salary", target = "salary")
    @Mapping(source = "hireDate", target = "hireDate")
    @Mapping(source = "status", target = "status")
    EmployeeResponse employeeToEmployeeResponse(Employee employee);
}


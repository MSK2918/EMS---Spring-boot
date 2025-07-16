package com.susheel.ems.mapper;


import com.susheel.ems.DTO.EmployeeDto;
import com.susheel.ems.DTO.EmployeeResponseDto;
import com.susheel.ems.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {
    // convert dto to employee
    public Employee employeeDtoToEmployee (EmployeeDto employeeDto) {

        if (employeeDto == null) {
            throw new NullPointerException("Employee Dto is required");
        }

        Employee employee = new Employee();
        employee.setName(employeeDto.name());
        employee.setEmail(employeeDto.email());
        employee.setAddress(employeeDto.address());
        employee.setSalary(employeeDto.salary());


        return employee;
    }

    // convert employee list to employee dto list
    public List<EmployeeResponseDto> employeeListToEmployeeResponseDtoList (List<Employee> employees) {
        List<EmployeeResponseDto> employeeResponseDtoList = new ArrayList<>();
        for (Employee employeeList : employees) {
            EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(
                    employeeList.getId(),
                    employeeList.getName(),
                    employeeList.getEmail(),
                    employeeList.getAddress(),
                    employeeList.getSalary()
            );
        employeeResponseDtoList.add(employeeResponseDto);
        }
        return employeeResponseDtoList;
    }

    // convert employee to employee dto
    public EmployeeResponseDto employeeToEmployeeResponseDto (Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getSalary()
        );
    }
}

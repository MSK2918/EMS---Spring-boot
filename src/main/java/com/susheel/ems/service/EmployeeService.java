package com.susheel.ems.service;

import java.util.UUID;

import com.susheel.ems.DTO.EmployeeDto;
import com.susheel.ems.DTO.EmployeeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    // save employee
    EmployeeResponseDto createEmployee(EmployeeDto employeeDto);

    // get all employees
    List<EmployeeResponseDto> getAllEmployees();

    // update employee by Id
    String updateEmployeeById(String id, EmployeeDto employeeDto);

    // delete employee by Id
    String deleteEmployeeById(String id);

    // delete all employees
    String deleteAllEmployees();
}






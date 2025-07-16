package com.susheel.ems.service.impl;

import java.util.Optional;
import java.util.UUID;

import com.susheel.ems.DTO.EmployeeDto;
import com.susheel.ems.DTO.EmployeeResponseDto;
import com.susheel.ems.entity.Employee;
import com.susheel.ems.exception.ResourceNotFoundException;
import com.susheel.ems.mapper.EmployeeMapper;
import com.susheel.ems.repository.EmployeeRepository;
import com.susheel.ems.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeResponseDto(savedEmployee);
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty())
            throw new NullPointerException();
        return employeeMapper.employeeListToEmployeeResponseDtoList(employees);
    }

    @Override
    public String updateEmployeeById(String id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", id.toString()));
        
        Employee updatedEmployee = employeeMapper.employeeDtoToEmployee(employeeDto);
        updatedEmployee.setId(id);
        
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        
        employeeRepository.save(existingEmployee);
        return "Employee updated successfully";
    }

    @Override
    public String deleteEmployeeById(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.delete(employee.get());
            return "Employee deleted successfully!!";
        } else {
            throw new ResourceNotFoundException("Employee", "Id",id);
        }
    }

    @Override
    public String deleteAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty())
            throw new NullPointerException();
        employeeRepository.deleteAll();
        return "Employee delete all success!!";
    }
}









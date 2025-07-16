package com.susheel.ems.service.impl;

import com.susheel.ems.DTO.EmployeeDto;
import com.susheel.ems.DTO.EmployeeResponseDto;
import com.susheel.ems.entity.Employee;
import com.susheel.ems.exception.ResourceNotFoundException;
import com.susheel.ems.mapper.EmployeeMapper;
import com.susheel.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    private EmployeeDto employeeDto;
    private Employee employee;
    private EmployeeResponseDto employeeResponseDto;
    private final String employeeId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto(
                "John Doe",
                "john.doe@example.com",
                "123 Main St, City",
                50000.0
        );

        employee = new Employee();
        employee.setId(employeeId);
        employee.setName(employeeDto.name());
        employee.setEmail(employeeDto.email());
        employee.setAddress(employeeDto.address());
        employee.setSalary(employeeDto.salary());

        employeeResponseDto = new EmployeeResponseDto(
                employeeId,
                employee.getName(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getSalary()
        );
    }

    @Test
    void saveEmployee_ValidDto_ReturnsEmployeeResponseDto() {
        // Arrange
        when(employeeMapper.employeeDtoToEmployee(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.employeeToEmployeeResponseDto(any(Employee.class))).thenReturn(employeeResponseDto);

        // Act
        EmployeeResponseDto result = employeeService.createEmployee(employeeDto);

        // Assert
        assertNotNull(result);
        assertEquals(employeeResponseDto, result);
        verify(employeeMapper).employeeDtoToEmployee(employeeDto);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).employeeToEmployeeResponseDto(employee);
    }

    @Test
    void getAllEmployees_EmployeesExist_ReturnsEmployeeList() {
        // Arrange
        List<Employee> employees = Arrays.asList(employee, employee);
        List<EmployeeResponseDto> expectedDtos = Arrays.asList(employeeResponseDto, employeeResponseDto);
        
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.employeeListToEmployeeResponseDtoList(employees)).thenReturn(expectedDtos);

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
        verify(employeeMapper).employeeListToEmployeeResponseDtoList(employees);
    }

    @Test
    void getAllEmployees_NoEmployees_ThrowsNullPointerException() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            employeeService.getAllEmployees();
        });
        
        verify(employeeRepository).findAll();
        verify(employeeMapper, never()).employeeListToEmployeeResponseDtoList(anyList());
    }

    @Test
    void updateEmployeeById_ValidId_ReturnsSuccessMessage() {
        // Arrange
        EmployeeDto updatedDto = new EmployeeDto(
                "Jane Doe",
                "jane.doe@example.com",
                "456 Oak St, Town",
                60000.0
        );

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeMapper.employeeDtoToEmployee(updatedDto)).thenReturn(new Employee(
                employeeId, updatedDto.name(), updatedDto.email(), updatedDto.address(), updatedDto.salary()
        ));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        String result = employeeService.updateEmployeeById(employeeId, updatedDto);

        // Assert
        assertEquals("Employee updated successfully", result);
        verify(employeeRepository).findById(employeeId);
        verify(employeeMapper).employeeDtoToEmployee(updatedDto);
        verify(employeeRepository).save(employeeCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        assertEquals(updatedDto.name(), savedEmployee.getName());
        assertEquals(updatedDto.email(), savedEmployee.getEmail());
        assertEquals(updatedDto.address(), savedEmployee.getAddress());
        assertEquals(updatedDto.salary(), savedEmployee.getSalary());
    }

    @Test
    void updateEmployeeById_NonExistentId_ThrowsResourceNotFoundException() {
        // Arrange
        String nonExistentId = UUID.randomUUID().toString();
        when(employeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployeeById(nonExistentId, employeeDto);
        });
        
        verify(employeeRepository).findById(nonExistentId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployeeById_ValidId_ReturnsSuccessMessage() {
        // Arrange
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);

        // Act
        String result = employeeService.deleteEmployeeById(employeeId);

        // Assert
        assertEquals("Employee deleted successfully!!", result);
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void deleteEmployeeById_NonExistentId_ThrowsResourceNotFoundException() {
        // Arrange
        String nonExistentId = UUID.randomUUID().toString();
        when(employeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployeeById(nonExistentId);
        });
        
        verify(employeeRepository).findById(nonExistentId);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void deleteAllEmployees_EmployeesExist_ReturnsSuccessMessage() {
        // Arrange
        List<Employee> employees = Arrays.asList(employee, employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        doNothing().when(employeeRepository).deleteAll();

        // Act
        String result = employeeService.deleteAllEmployees();

        // Assert
        assertEquals("Employee delete all success!!", result);
        verify(employeeRepository).findAll();
        verify(employeeRepository).deleteAll();
    }

    @Test
    void deleteAllEmployees_NoEmployees_ThrowsNullPointerException() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            employeeService.deleteAllEmployees();
        });
        
        verify(employeeRepository).findAll();
        verify(employeeRepository, never()).deleteAll();
    }
}

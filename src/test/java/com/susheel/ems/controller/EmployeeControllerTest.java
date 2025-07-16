package com.susheel.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.susheel.ems.DTO.EmployeeDto;
import com.susheel.ems.DTO.EmployeeResponseDto;
import com.susheel.ems.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EmployeeDto employeeDto;
    private EmployeeResponseDto employeeResponseDto;
    private final String baseUrl = "/api/v1/employee";
    private final String deleteUrl = "/api/v1";
    private final String employeeId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
        
        employeeDto = new EmployeeDto(
                "John Doe",
                "john.doe@example.com",
                "123 Main St, City",
                50000.0
        );
        
        employeeResponseDto = new EmployeeResponseDto(
                employeeId,
                employeeDto.name(),
                employeeDto.email(),
                employeeDto.address(),
                employeeDto.salary()
        );
    }

    @Test
    void saveEmployee_ValidInput_ReturnsSuccessMessage() throws Exception {
        String successMessage = "Employee saved successfully with id: " + employeeId;
        when(employeeService.createEmployee(employeeDto)).thenReturn(employeeResponseDto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeId)));

        verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class));
    }

    @Test
    void getAllEmployees_ReturnsListOfEmployees() throws Exception {
        List<EmployeeResponseDto> employees = Arrays.asList(employeeResponseDto, employeeResponseDto);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(employeeId.toString())));
    }

    @Test
    void getAllEmployees_NoEmployees_ReturnsEmptyList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void updateEmployeeById_ValidInput_ReturnsSuccessMessage() throws Exception {
        String successMessage = "Employee updated successfully!!";
        when(employeeService.updateEmployeeById(employeeId, employeeDto)).thenReturn("Employee updated successfully!!");

        mockMvc.perform(put("/api/v1/update/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(successMessage)));
    }

    @Test
    void deleteEmployeeById_ValidId_ReturnsSuccessMessage() throws Exception {
        String successMessage = "Employee deleted successfully with!!";
        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(successMessage);

        mockMvc.perform(delete(deleteUrl + "/delete/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(successMessage)));
    }

    @Test
    void deleteAllEmployees_ReturnsSuccessMessage() throws Exception {
        String successMessage = "All employees deleted successfully";
        when(employeeService.deleteAllEmployees()).thenReturn(successMessage);

        mockMvc.perform(delete(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(successMessage)));
    }
}


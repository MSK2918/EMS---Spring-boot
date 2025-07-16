package com.susheel.ems.DTO;

import jakarta.validation.constraints.*;

public record EmployeeDto(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Address is required")
        @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
        String address,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be positive")
        Double salary
) {
}



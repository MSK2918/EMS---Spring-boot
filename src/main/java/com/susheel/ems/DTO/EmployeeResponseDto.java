package com.susheel.ems.DTO;

import java.util.UUID;

public record EmployeeResponseDto(
        String id,
        String name,
        String email,
        String address,
        Double salary
) {
}



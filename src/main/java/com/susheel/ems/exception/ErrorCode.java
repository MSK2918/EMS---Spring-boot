package com.susheel.ems.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    EMPTY_EMPLOYEES(204, HttpStatus.BAD_REQUEST, "Employees list is empty"),
    DUPLICATE_FIELD(307, HttpStatus.FORBIDDEN, "Looks like this email is already taken, try a different one"),
    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!, try again later")
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;


    ErrorCode(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}

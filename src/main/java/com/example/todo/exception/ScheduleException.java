package com.example.todo.exception;

import lombok.Getter;

@Getter
public class ScheduleException extends RuntimeException{
    private final ErrorCode errorCode;

    public ScheduleException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}

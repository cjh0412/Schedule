package com.example.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUNT_DATA(HttpStatus.NOT_FOUND, "수정된 값이 존재하지 않습니다."),
    PASSWORD_CHECK_FAIL(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    ALREADY_DELETE_DATE(HttpStatus.NOT_FOUND, "이미 삭제된 데이터 입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(HttpStatus httpStatus, String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}

package com.ll.medium.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());      // RuntimeException 의 message 필드에 저장
        this.errorCode = errorCode;
    }
}

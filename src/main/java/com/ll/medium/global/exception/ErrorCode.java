package com.ll.medium.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 번호의 글이 존재하지 않습니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "해당 이름은 이미 사용 중 입니다."),
    MEMBER_POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "회원님이 작성한 글이 없습니다."),
    EDIT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    DELETE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

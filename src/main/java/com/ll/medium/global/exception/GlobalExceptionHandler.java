package com.ll.medium.global.exception;

import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
@Order(-1)    // 다른 예외 처리 클래스보다 우선 순위로 적용되는 클래스
public class GlobalExceptionHandler {
    private final Rq rq;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors()
                .forEach((error -> errors.add(error.getDefaultMessage())));

        String joinedErrors = String.join(", ", errors);
        log.error(errors.toString());

        return rq.historyBack(joinedErrors);
    }

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException exception) {
        log.error(exception.getErrorCode() + " : " + exception.getErrorCode().getMessage());
        return rq.historyBack(exception);
    }
}

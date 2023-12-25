package com.ll.medium.global.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class Ut {
    public static class exception {
        public static String toString(Exception e) {    // Exception 객체의 상세 정보를 포맷된 문자열로 반환한다.
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();

            StringBuilder details = new StringBuilder();

            // 예외 메시지 추가 ex) Exception Message: Index 10 out of bounds for length 5
            details.append("Exception Message: ").append(e.getMessage()).append("\n");
            // 예외 원인 추가 ex) Caused by: java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 5
            Optional.ofNullable(e.getCause())
                    .ifPresent(cause -> {
                        details.append("Caused by: ").append(cause).append("\n");
                    });
            // 스택 트레이스 추가
            details.append("StackTrace:\n").append(stackTrace);

            return details.toString();
        }
    }
}

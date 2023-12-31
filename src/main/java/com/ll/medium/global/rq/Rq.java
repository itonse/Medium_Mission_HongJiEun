package com.ll.medium.global.rq;

import com.ll.medium.global.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequestScope   // HTTP 요청마다 객체의 인스턴스가 새롭게 생성되고, 끝날 때 제거된다(다른 요청과의 데이터 충돌 방지)
@Component
@Getter
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    public User getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)    // 현재 인증된 사용자에 대한 정보를 담고있는 Authentication 객체를 꺼내고
                .map(Authentication::getPrincipal)     // 객체의 주체를 꺼내서
                .filter(it -> it instanceof User)    // 주체가 User 인스턴스인지 확인 후, 아니라면 orElse로 바로 간다
                .map(it -> (User) it)      // Object 타입의 principal을 User 타입으로 변환
                .orElse(null);     // 이전의 filter에서 걸러졌으면 null 반환
    }

    public boolean isLogined() {
        return getUser() != null;
    }

    public boolean isLogout() {
        return !isLogined();
    }

    // 오류 메시지와 함께 이전 페이지로 돌아가기
    public String historyBack(String msg) {
        resp.setStatus(400);    // 응답 상태코드를 400으로 설정
        req.setAttribute("msg", msg);

        return "global/js";
    }

    // 예외 메시지와 함께 이전 페이지로 돌아가기
    public String historyBack(CustomException e) {
        resp.setStatus(e.getErrorCode().getHttpStatus().value());   // 응답 상태코드 지정
        req.setAttribute("exceptionStr", e.getErrorCode().getMessage());

        return "global/js";
    }

    // 메시지와 함께 리다이렉트 경로 반환
    public String redirect(String url, String msg) {
        StringBuilder sb = new StringBuilder();

        sb.append("redirect:");
        sb.append(url);

        if (!msg.isBlank()) {
            msg = URLEncoder.encode(msg, StandardCharsets.UTF_8);
            sb.append("?msg=");
            sb.append(msg);
        }

        return sb.toString();
    }

    /*
    // HTTP 요청 속성을 설정
    public void setAttribute(String key, Object value) {
        req.setAttribute(key, value);
    }
     */
}

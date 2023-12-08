package com.ll.medium.global.rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@RequestScope   // 이 컴포넌트의 라이프사이클이 HTTP 요청에 바인딩되어 있음
@Component
@Getter
@RequiredArgsConstructor
@Slf4j
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    public User getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(it -> it instanceof User)
                .map(it -> (User) it)
                .orElse(null);
    }

    public boolean isLogined() {
        return getUser() != null;
    }

    public boolean isLogout() {
        return !isLogined();
    }
}

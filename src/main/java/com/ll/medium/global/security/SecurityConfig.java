package com.ll.medium.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(
                                frameOptions ->
                                        frameOptions.sameOrigin()
                        )
                )
                .csrf(csrf -> csrf    // CSRF 비활성화 경로 설정
                        .ignoringRequestMatchers(
                                "/h2-console/**", "/member/join"
                        ))
                .formLogin(formLogin -> formLogin    // 사용자 정의 로그인 페이지
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/")    // 로그인 성공 시 리다이렉트 경로
                        .failureUrl("/member/login?error"))  // 로그인 실패 시 URL 설정
                .logout(logout -> logout     // 사용자 정의 로그아웃
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                        .logoutSuccessUrl("/")     // 로그아웃 시 리다이렉트 경로
                        .invalidateHttpSession(true))     // 로그아웃 시 사용자 세션 삭제
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

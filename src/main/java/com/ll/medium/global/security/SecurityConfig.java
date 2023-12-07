package com.ll.medium.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests   // 인증 요구 사항 설정
                        .requestMatchers("/**")
                        .permitAll()   // 모든 요청에 대해 접근 서용
                )
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
                        .defaultSuccessUrl("/"))    // 로그인 성공 시 리다이렉트 경로
        ;

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

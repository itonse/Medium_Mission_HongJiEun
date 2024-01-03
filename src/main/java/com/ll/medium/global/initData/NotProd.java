package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Profile("!prod")
public class NotProd {
    private static final int TOTAL_MEMBERS = 200;

    // 클래스 내부에서 메서드를 직접 호출할 경우 Spring의 프록시 기반 AOP가 적용되지 않는 문제가 발생.
    // AOP 프록시 기능 활성화를 위해 Self injection 사용하여 문제 해결
    @Autowired
    @Lazy  // 순환 참조 방지
    private NotProd self;
    private final MemberService memberService;

    @Autowired
    private DataInitService dataInitService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            if (!memberService.existsByUsername("user1")) {
                self.generateTestData();   // self를 사용해 내부 메서드 호출 시 @Transactional 기능 활성화
            }
        };
    }

    @Transactional
    public void generateTestData() {
        for (int i = 1; i <= TOTAL_MEMBERS; i++) {
            dataInitService.createMemberAndPost(i);
        }
    }
}

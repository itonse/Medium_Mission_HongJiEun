package com.ll.medium.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync   // 비동기 처리 기능 활성화
public class AsyncConfig {    // 비동기 작업을 위한 스레드 풀 구성

    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 항상 실행 중인 스레드의 최소 수 (CPU의 코어 수를 기반으로 설정)
        executor.setMaxPoolSize(15);    // 동시에 실행될 수 있는 최대 스레드 수 (코어 수의 2 ~ 4배가 일반적)
        executor.setQueueCapacity(500);   // 스레드 풀에서 처리될 때까지 대기하는 작업의 최대 수
        executor.setThreadNamePrefix("Executor-");    // 의미 있는 이름 (디버깅 및 로깅 목적)

        executor.initialize();    // 스레드 풀을 초기화 및 사용 준비

        return executor;
    }
}

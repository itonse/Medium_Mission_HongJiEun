package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Profile("!prod")
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    public ApplicationRunner initNotProd() {
        return new ApplicationRunner() {
            @Transactional
            @Override
            public void run(ApplicationArguments args) throws Exception {
                if (memberService.existsByUsername("user1")) return;

                Random random = new Random();

                // Member 객체 생성
                for (int i = 1; i <= 100; i++) {
                    Member member = Member.builder()
                            .username("user" + i)
                            .password("password" + i)
                            .email("user" + i + "@example.com")
                            .verified(true)
                            .paid(i % 2 == 0)
                            .build();
                    memberService.save(member);

                    // Post 객체 생성
                    for (int j = 0; j < 2; j++) {
                        boolean postPaid = j % 2 == 0;
                        // paid가 false인 member는 paid가 false인 post만 생성
                        if (member.isPaid()) {
                            postPaid = random.nextBoolean(); // 멤버십 회원인 경우 유료글 여부는 랜덤
                        } else {
                            postPaid = false; // 멤버십 회원이 아니면 항상 일반글
                        }

                        int postNumber = (i - 1) * 2 + j + 1;
                        Post post = Post.builder()
                                .title("Post Title " + postNumber)
                                .body("Post Body " + postNumber)
                                .published(random.nextBoolean())    // 공개글 여부는 랜덤
                                .paid(postPaid)
                                .author(member)
                                .build();

                        postService.save(post);
                    }
                }
            }
        };
    }

}

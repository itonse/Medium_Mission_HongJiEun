package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DataInitService {
    private static final int POSTS_PER_MEMBER = 1;   // 한 멤버가 작성할 글의 수

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Async("threadPoolTaskExecutor")   // 비동기 실행을 위해 해당 이름의 스레드 풀 사용
    public void createMemberAndPost(int memberIndex) {
        Random random = new Random();

        // Member 객체 생성 및 저장
        Member member = Member.builder()
                .username("user" + memberIndex)
                .password(passwordEncoder.encode("password" + memberIndex))
                .email("user" + memberIndex + "@example.com")
                .verified(true)
                .paid(memberIndex % 2 == 0)    // 멤버십 회원과 일반회원의 비율은 1:1
                .build();

        memberService.save(member);

        // Post 객체 생성 및 저장
        for (int j = 0; j < POSTS_PER_MEMBER; j++) {
            boolean postPaid = member.isPaid(); // 멤버십 회원인 경우 유료글만 작성
            int postNumber = (memberIndex - 1) * POSTS_PER_MEMBER + j + 1;

            Post post = Post.builder()
                    .title("Post Title " + postNumber)
                    .body("Post Body " + postNumber)
                    .published(random.nextBoolean())   // 공개글 여부는 랜덤
                    .paid(postPaid)
                    .author(member)
                    .build();

            postService.save(post);
        }
    }
}

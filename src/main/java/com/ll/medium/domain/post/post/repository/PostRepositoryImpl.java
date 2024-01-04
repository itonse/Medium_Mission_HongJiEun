package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostDto> search(List<String> kwTypes, String kw, Pageable pageable) {
        QPost qPost = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        // published가 true인 글만 가져오기
        builder.and(qPost.published.isTrue());

        // 검색 조건 설정
        if (kwTypes.contains("title")) {
            builder.and(qPost.title.containsIgnoreCase(kw));
        }
        if (kwTypes.contains("body")) {
            builder.and(qPost.body.containsIgnoreCase(kw));
        }

        // 쿼리 생성
        JPAQuery<Post> query = jpaQueryFactory
                .selectFrom(qPost)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 조건 적용
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(qPost.getType(), qPost.getMetadata());
            query.orderBy(new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty())
            ));
        }


        List<Post> posts = query.fetch();    // 조건에 맞는 Post 객체들을 DB에서 조회하여 리스트로 반환

        long total = query.fetchCount();    // 조건에 해당하는 Post 객체들의 총 개수를 DB에서 조회

        Page<Post> postsPage = new PageImpl<>(posts, pageable, total);   // 조회된 Post 객체들과 페이징 정보를 이용하여 Page<Post> 객체 생성

        return PostDto.toDtoPage(postsPage);    // Page<Post> 객체를 Page<PostDto> 객체로 변환하여 반환
    }
}

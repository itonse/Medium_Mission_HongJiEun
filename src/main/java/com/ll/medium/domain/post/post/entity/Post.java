package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AuditOverride(forClass = BaseTimeEntity.class)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;
    private String body;
    private boolean published;

    @ManyToOne(fetch = FetchType.LAZY)
    Member author;

    public void updatePost(String title, String body, boolean published) {
        this.title = title;
        this.body = body;
        this.published = published;
    }
}

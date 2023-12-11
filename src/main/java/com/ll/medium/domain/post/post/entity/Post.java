package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String title;
    private String body;
    private boolean isPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    Member author;
}

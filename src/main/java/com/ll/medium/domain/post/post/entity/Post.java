package com.ll.medium.domain.post.post.entity;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;

import static lombok.AccessLevel.PROTECTED;

@Entity
@SuperBuilder
@AuditOverride(forClass = BaseEntity.class)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String title;
    private String body;
    private boolean published;

    @ManyToOne(fetch = FetchType.LAZY)
    Member author;
}

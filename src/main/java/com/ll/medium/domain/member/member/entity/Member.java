package com.ll.medium.domain.member.member.entity;

import com.ll.medium.global.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AuditOverride(forClass = BaseTimeEntity.class)
@AllArgsConstructor(access = PROTECTED)    // builer로만 객체 생성하도록 강제 (외부에서 무분별한 객체 생성 방지)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private boolean verified;
}

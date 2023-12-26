package com.ll.medium.domain.member.member.entity;

import com.ll.medium.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor(access = PROTECTED)    // builer로만 객체 생성하도록 강제 (외부에서 무분별한 객체 생성 방지)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private boolean verified;
}

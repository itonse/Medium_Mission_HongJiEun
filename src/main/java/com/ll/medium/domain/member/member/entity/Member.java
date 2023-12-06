package com.ll.medium.domain.member.member.entity;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.global.jpa.baseEntity.BaseEntity;
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
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private boolean verified;

    public static Member from(JoinForm form) {
        return Member.builder()
                .username(form.getUsername())
                .password(form.getPassword())
                .email(form.getEmail())
                .verified(false)
                .build();
    }
}

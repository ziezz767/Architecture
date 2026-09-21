package com.example.architecture.repository;

import com.example.architecture.common.context.UserContext;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/*
    < 세상 모든 엔티티들이 공통적으로 갖는 필드 >
    - id
    - deleted
    - createdAt : 언제 '생성'되었는가?
    - createdBy : 누가 '생성'하였는가?
    - updatedAt : 언제 '갱신'되었는가?
    - updatedBy : 누가 '갱신'하였는가?
*/
@ToString
@Getter
public class BaseEntity {
    protected Integer id;
    protected boolean deleted = false;
    protected LocalDateTime createdAt;
    protected       Integer createdBy;
    protected LocalDateTime updatedAt;
    protected       Integer updatedBy;

    protected BaseEntity(Integer id, Integer userId) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    protected void updated() {
        Integer currentUserId = UserContext.getUserId();
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = currentUserId;
    }
}

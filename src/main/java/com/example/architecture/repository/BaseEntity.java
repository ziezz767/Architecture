package com.example.architecture.repository;

import lombok.Getter;

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
@Getter
public class BaseEntity {
    private Integer id;
    private boolean deleted = false;
    private LocalDateTime createdAt;
    private       Integer createdBy;
    private LocalDateTime updatedAt;
    private       Integer updatedBy;

    protected BaseEntity(Integer id, Integer userId) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    public void updated(Integer userId) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }
}

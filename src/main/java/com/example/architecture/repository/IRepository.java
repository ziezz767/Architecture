package com.example.architecture.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<ID, ENTITY> {
    // 전체 조회
    List<ENTITY> findAll();

    // 단일 조회
    Optional<ENTITY> findById(ID id);

    // 단일 생성
    Optional<ENTITY> create(ENTITY entity);

    // 단일 갱신
    Optional<ENTITY> update(ENTITY entity);

    // 단일 삭제
    void delete(ID id);
}

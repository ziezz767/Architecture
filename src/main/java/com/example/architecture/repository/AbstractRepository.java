package com.example.architecture.repository;

import java.util.*;

public class AbstractRepository<ENTITY extends BaseEntity> implements IRepository<Integer, ENTITY>{
    private final Map<Integer, ENTITY> database = new HashMap<>();

    // < CRUD 기능 >
    // R - 전체 조회
    @Override
    public List<ENTITY> findAll() {
        return this.database.values()
                .stream()
                .toList();
    }

    // R - 단일 조회
    @Override
    public Optional<ENTITY> findById(Integer id) {
        return Optional.ofNullable(this.database.get(id));
    }

    // C - 단일 생성
    @Override
    public Optional<ENTITY> create(ENTITY entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 결제가 이미 존재합니다." + id);
        }
        this.database.put(id, entity);
        ENTITY created = this.database.get(id);
        return Optional.ofNullable(created);
    }

    // U - 단일 갱신
    @Override
    public Optional<ENTITY> update(ENTITY entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 결제가 존재하지 않습니다." + id);
        }
        this.database.replace(id, entity);
        ENTITY updated = this.database.get(id);
        return Optional.ofNullable(updated);
    }

    // D - 단일 삭제
    @Override
    public void delete(Integer id) {
        if (Objects.isNull(this.database.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 결제가 존재하지 않습니다." + id);
        }
        this.database.remove(id);
    }
}

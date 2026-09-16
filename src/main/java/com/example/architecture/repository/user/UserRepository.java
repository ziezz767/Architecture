package com.example.architecture.repository.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Integer, User> USERS = new HashMap<>();

    // < CRUD 기능 >
    // R - 전체 조회
    public List<User> findAll() {
        return USERS.values()
                .stream()
                .toList();
    }

    // R - 단일 조회
    public Optional<User> findById(User entity) {
        Integer id = entity.getId();
        return Optional.ofNullable(USERS.get(id));
    }

    // C - 단일 생성
    public Optional<User> create(User entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(USERS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 유저가 이미 존재합니다." + id);
        }
        return Optional.ofNullable(USERS.put(id, entity));
    }

    // U - 단일 수정
    public Optional<User> update(User entity) {
        Integer id = entity.getId();
        if (Objects.isNull(USERS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 유저가 존재하지 않습니다." + id);
        }
        return Optional.ofNullable(USERS.replace(id, entity));
    }

    // D - 단일 삭제
    public void remove(Integer id) {
        if (Objects.isNull(USERS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 유저가 존재하지 않습니다." + id);
        }
        USERS.remove(id);
    }
}

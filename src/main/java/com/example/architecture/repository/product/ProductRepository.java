package com.example.architecture.repository.product;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {
    private final Map<Integer, Product> PRODUCTS = new HashMap<>();

    // < CRUD 기능 >
    // R - 전체 조회
    public List<Product> findAll() {
        return PRODUCTS.values()
                .stream()
                .toList();
    }

    // R - 단일 조회
    public Optional<Product> findById(Integer id) {
        return Optional.ofNullable(PRODUCTS.get(id));
    }

    // C - 단일 생성
    public Optional<Product> create(Product entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(PRODUCTS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 상품이 이미 존재합니다." + id);
        }
        return Optional.ofNullable(PRODUCTS.put(id, entity));
    }

    // U - 단일 수정
    public Optional<Product> update(Product entity) {
        Integer id = entity.getId();
        if (Objects.isNull(PRODUCTS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 상품이 존재하지 않습니다." + id);
        }
        return Optional.ofNullable(PRODUCTS.replace(id, entity));
    }

    // D - 단일 삭제
    public void remove(Integer id) {
        if (Objects.isNull(PRODUCTS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 상품이 존재하지 않습니다." + id);
        }
        PRODUCTS.remove(id);
    }
}

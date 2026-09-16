package com.example.architecture.repository.payment;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentRepository {
    private final Map<Integer, Payment> PAYMENTS = new HashMap<> ();

    // < CRUD 기능 >
    // R - 전체 조회
    public List<Payment> findAll() {
        return PAYMENTS.values()
                .stream()
                .toList();
    }

    // R - 단일 조회
    public Optional<Payment> findById(Integer id) {
        return Optional.ofNullable(PAYMENTS.get(id));
    }

    // C - 단일 생성
    public Optional<Payment> create(Payment entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(PAYMENTS.get(id))) {
            throw new RuntimeException("기존에 해당하는 아이디를 가진 결제가 이미 존재합니다." + id);
        }
        return Optional.ofNullable(PAYMENTS.put(id, entity));
    }

    // U - 단일 갱신
    public Optional<Payment> update(Payment entity) {
        Integer id = entity.getId();
        if (Objects.nonNull(PAYMENTS.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 결제가 존재하지 않습니다." + id);
        }
        Payment updated = PAYMENTS.replace(id, entity);
        return Optional.ofNullable(updated);
    }

    // D - 단일 삭제
    public void remove(Integer id) {
        if (Objects.isNull(PAYMENTS.get(id))) {
            throw new RuntimeException("기존에 해당 아이디를 가진 결제가 존재하지 않습니다." + id);
        }
        PAYMENTS.remove(id);
    }
}

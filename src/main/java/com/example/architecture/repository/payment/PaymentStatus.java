package com.example.architecture.repository.payment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum PaymentStatus {
    IN_PAYMENT("결제 중", true),
    PAYMENT_COMPLETE("결제 완료", true),
    IN_DELIVERY("배송 중", false),
    DELIVERY_COMPLETE("배송 완료", false),
    CANCEL_COMPLETE("취소 완료", false);


    String description;     // 현재 결제 상태 설명
    boolean cancellable;    // 취소 가능 여부
}

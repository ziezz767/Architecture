package com.example.architecture.application.payment;

import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;

import java.util.List;

public interface IPaymentApplication {
    // 결제
    PaymentResponseDto payment(List<Integer> productIds);

    // 결제 취소
    PaymentResponseDto cancel(Integer id);
}

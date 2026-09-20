package com.example.architecture.service.payment;

import com.example.architecture.repository.payment.Payment;
import com.example.architecture.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    // findById
    public Payment getPayment(Integer id) {
        Optional<Payment> WrappedPayment = paymentRepository.findById(id);
        Payment         payment = WrappedPayment
                .orElseThrow(() -> new RuntimeException("찾으시는 결제가 존재하지 않습니다."));
        return payment;
    }

    // create
    public Payment create(Payment entity) {
        Optional<Payment> WrappedCreated = paymentRepository.create(entity);
                 Payment         created = WrappedCreated
                .orElseThrow(() -> new RuntimeException("결제가 정상적으로 생성되지 않습니다."));
        return created;
    }

    // update
    public Payment update(Payment entity) {
        Optional<Payment> WrappedPayment = paymentRepository.update(entity);
                 Payment         payment = WrappedPayment
                .orElseThrow(() -> new RuntimeException("업데이트가 정상적으로 되지 않습니다."));
        return payment;
    }
}

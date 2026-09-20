package com.example.architecture.service.payment;

import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;
import com.example.architecture.repository.payment.Payment;
import com.example.architecture.repository.payment.PaymentRepository;
import com.example.architecture.repository.payment.PaymentStatus;
import com.example.architecture.repository.product.Product;
import com.example.architecture.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    // 결제
    public PaymentResponseDto payment(List<Integer> productIds, Integer requestedUserId) {
        // 1. 구매해려는 상품이 존재하는지 + 상품의 재고가 충분한지 검증
        List<Product> products = new ArrayList<>();

        for (Integer productId : productIds) {
            Optional<Product> WrappedProduct =  productRepository.findById(productId);
            Product         product = WrappedProduct
                    .orElseThrow(() -> new RuntimeException("찾으시는 상품이 존재하지 않습니다."));
            if (product.getStock() < 1) {
                throw new RuntimeException("구매하시려는 상품의 재고가 존재하지 않습니다.");
            }
            products.add(product);
        }

        // 2. 실제 구매 완료
        Payment creating = Payment.create(products, requestedUserId);
        creating.setStatus(PaymentStatus.PAYMENT_COMPLETE);
        creating.setPurchasedAt(LocalDateTime.now());
        creating.updated(requestedUserId);
        Optional<Payment> WrappedPayment = paymentRepository.create(creating);
        Payment         payment = WrappedPayment
                .orElseThrow(() -> new RuntimeException("결제가 정상적으로 생성되지 않습니다."));

        // 3. 구매가 완료된 상품들에 대해서 재고 1개씩 차감
        for (Product product : products) {
            product.setStock(product.getStock() - 1);
            productRepository.update(product);
        }

        // 4. Dto로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(payment)
                .products(products)
                .build();
    }

    // 결제 취소
    public PaymentResponseDto cancel(Integer id, Integer requestedUserId) {
        // 1. 취소하시려는 결제건이 존재하는지 확인
        Optional<Payment> WrappedPayment = paymentRepository.findById(id);
        Payment         payment = WrappedPayment
                .orElseThrow(() -> new RuntimeException("취소하려는 결제가 존재하지 않습니다."));

        // 2. 취소 완료 (결제 엔티티의 일반 필드들 상태 변환)
        PaymentStatus currentStatus = payment.getStatus();
        if (!currentStatus.isCancellable()) {
            throw new RuntimeException("취소하시려는 결제는 취소할 수 없는 상태입니다.");
        }
        payment.setStatus(PaymentStatus.CANCEL_COMPLETE);
        payment.setCancelledAt(LocalDateTime.now());
        payment.updated(requestedUserId);

        // 3. 취소한 결제건에 들어있던 모든 상품들의 재고를 1 증가시키며 롤백 (다른 엔티티의 필드들 상태 변환)
        List<Product> products = new ArrayList<>();
        List<Integer> productIds = payment.getProductIds();
        for (Integer productId : productIds) {
            Optional<Product> WrappedProduct = productRepository.findById(productId);
            Product         product = WrappedProduct
                    .orElseThrow(() -> new RuntimeException("결제한 상품이 존재하지 않습니다."));
            product.setStock(product.getStock() + 1);
            productRepository.update(product);
            products.add(product);
        }

        // 4. DTO 로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(payment)
                .products(products)
                .build();
    }
}

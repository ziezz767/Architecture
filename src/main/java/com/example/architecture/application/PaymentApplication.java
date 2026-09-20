package com.example.architecture.application;

import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;
import com.example.architecture.repository.payment.Payment;
import com.example.architecture.repository.payment.PaymentStatus;
import com.example.architecture.repository.product.Product;
import com.example.architecture.service.payment.PaymentService;
import com.example.architecture.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentApplication {
    private final PaymentService paymentService;
    private final ProductService productService;

    // 결제
    public PaymentResponseDto payment(List<Integer> productIds, Integer requestedUserId) {
        // 1. 구매해려는 상품이 존재하는지 + 상품의 재고가 충분한지 검증
        List<Product> products = new ArrayList<>();

        for (Integer productId : productIds) {
            Product product =  productService.getProduct(productId);
            product.buyable();
            products.add(product);
        }

        // 2. 실제 구매 완료
        Payment creating = Payment.create(products, requestedUserId);
        creating.complete(requestedUserId);
        Payment created = paymentService.create(creating);

        // 3. 구매가 완료된 상품들에 대해서 재고 1개씩 차감
        for (Product product : products) {
            product.decrease();
            productService.update(product);
        }

        // 4. Dto로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(created)
                .products(products)
                .build();
    }

    // 결제 취소
    public PaymentResponseDto cancel(Integer id, Integer requestedUserId) {
        // 1. 취소하시려는 결제건이 존재하는지 확인
        Payment payment = paymentService.getPayment(id);

        // 2. 취소 완료 (결제 엔티티의 일반 필드들 상태 변환)
        payment.cancel(requestedUserId);
        paymentService.update(payment);

        // 3. 취소한 결제건에 들어있던 모든 상품들의 재고를 1 증가시키며 롤백 (다른 엔티티의 필드들 상태 변환)
        List<Product> products = new ArrayList<>();
        List<Integer> productIds = payment.getProductIds();
        for (Integer productId : productIds) {
            Product product = productService.getProduct(productId);
            product.increase();
            productService.update(product);
            products.add(product);
        }

        // 4. DTO 로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(payment)
                .products(products)
                .build();
    }
}

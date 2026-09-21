package com.example.architecture.application.payment;

import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;
import com.example.architecture.repository.payment.Payment;
import com.example.architecture.repository.product.Product;
import com.example.architecture.service.payment.PaymentService;
import com.example.architecture.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentApplication implements IPaymentApplication{
    private final PaymentService paymentService;
    private final ProductService productService;

    // 결제
    public PaymentResponseDto payment(List<Integer> productIds) {
        // 1. 구매해려는 상품이 존재하는지 + 상품의 재고가 충분한지 검증 + 구매한 상품들에 대해서 재고 1개씩 차감
        List<Product> updatedProducts = productIds.stream()
                .map((productId) -> {
                    Product product = productService.getProduct(productId);
                    product.buyable();
                    product.decrease();
                    return product;
                })
                .toList();
        productService.update(updatedProducts);

        // 2. 실제 구매 완료
        Payment creating = Payment.create(updatedProducts);
        creating.complete();

        Payment createdPayment = paymentService.create(creating);

        // 3. Dto로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(createdPayment)
                .products(updatedProducts)
                .build();
    }

    // 결제 취소
    public PaymentResponseDto cancel(Integer id) {
        // 1. 취소하시려는 결제건이 존재하는지 확인
        Payment cancelledPayment = paymentService.getPayment(id);

        // 2. 취소 완료 (결제 엔티티의 일반 필드들 상태 변환)
        cancelledPayment.cancel();
        paymentService.update(cancelledPayment);

        // 3. 취소한 결제건에 들어있던 모든 상품들의 재고를 1 증가시키며 롤백 (다른 엔티티의 필드들 상태 변환)
        List<Product> rollbackProducts = cancelledPayment.getProductIds().stream()
                .map((productId) -> {
                    Product product = productService.getProduct(productId);
                    product.increase();
                    return product;
                })
                .toList();
        productService.update(rollbackProducts);

        // 4. DTO 로 변환 후, 반환
        return PaymentResponseDto.builder()
                .payment(cancelledPayment)
                .products(rollbackProducts)
                .build();
    }
}

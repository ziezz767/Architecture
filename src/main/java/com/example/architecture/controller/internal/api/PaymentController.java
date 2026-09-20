package com.example.architecture.controller.internal.api;

import com.example.architecture.controller.internal.api.dto.PaymentCreateRequestDto;
import com.example.architecture.controller.internal.api.dto.PaymentResponseDto;
import com.example.architecture.repository.payment.Payment;
import com.example.architecture.repository.payment.PaymentRepository;
import com.example.architecture.repository.payment.PaymentStatus;
import com.example.architecture.repository.product.Product;
import com.example.architecture.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    < 실제 고객의 구매와 구매 취소 기능 / 버튼에 대한 API 제공 >
    - 고객이 어떤 상품들을 구매할지 보내면 그 고객에게 해당 상품의 구매 정보를 생성
    - 고객이 기존에 구매했던 구매 건을 취소하는 경우 - Hard Delete 가 아닌 Soft Delete 상태로 변경
*/

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    // 결제
    @RequestMapping(method = RequestMethod.POST, value = "/internal/api/payments")
    public PaymentResponseDto payment(@RequestBody PaymentCreateRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();

        // 1. 구매해려는 상품이 존재하는지 + 상품의 재고가 충분한지 검증
        List<Product> products = new ArrayList<>();
        List<Integer> productIds = request.getProductIds();
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
    @RequestMapping(method = RequestMethod.PATCH, value = "/internal/api/payments/{id}/cancel")
    public PaymentResponseDto cancel(@PathVariable Integer id, @RequestBody PaymentCreateRequestDto request) {
        Integer requestedUserId = request.getRequestUserId();

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

package com.example.architecture.repository.payment;

import com.example.architecture.repository.product.Product;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Payment {
    private static int PAYMENT_CURRENT_ID = 0;
    private static int idGenerator() {
        return ++PAYMENT_CURRENT_ID;
    }

    private Integer id;
    private List<Integer> productIds;
    private PaymentStatus status = PaymentStatus.IN_PAYMENT;
    private int paidPrice;
    private LocalDateTime purchasedAt;  // 결제 완료 시점
    private LocalDateTime deliveredAt;  // 배송 완료 시점
    private LocalDateTime cancelledAt;  // 취소 완료 시점
    private boolean deleted = false;

    private Payment(Integer id, List<Integer> productIds, int paidPrice) {
        this.id = id;
        this.productIds = productIds;
        this.paidPrice = paidPrice;
    }

    public static Payment create(List<Product> products) {
        int generatedId = idGenerator();

        List<Integer> productIds = products.stream()
                .map(Product::getId)
                .toList();

        int paidPrice = products.stream()
                .map(Product::getPrice)
                .reduce(0, Integer::sum);

        return new Payment(generatedId, productIds, paidPrice);
    }
}

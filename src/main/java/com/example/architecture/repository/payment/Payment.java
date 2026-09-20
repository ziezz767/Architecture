package com.example.architecture.repository.payment;

import com.example.architecture.repository.BaseEntity;
import com.example.architecture.repository.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString(callSuper = true)
@Getter
public class Payment extends BaseEntity {
    private static int PAYMENT_CURRENT_ID = 0;
    private static int idGenerator() {
        return ++PAYMENT_CURRENT_ID;
    }

    private List<Integer> productIds;
    private int paidPrice;

    @Setter
    private PaymentStatus status = PaymentStatus.IN_PAYMENT;

    @Setter
    private LocalDateTime purchasedAt;  // 결제 완료 시점

    @Setter
    private LocalDateTime deliveredAt;  // 배송 완료 시점

    @Setter
    private LocalDateTime cancelledAt;  // 취소 완료 시점

    private Payment(Integer id, List<Integer> productIds, int paidPrice, Integer userId) {
        super(id, userId);
        this.productIds = productIds;
        this.paidPrice = paidPrice;
    }

    // userId => 누가 구매를 하였는지
    public static Payment create(List<Product> products, Integer userId) {
        int generatedId = idGenerator();

        List<Integer> productIds = products.stream()
                .map(Product::getId)
                .toList();

        int paidPrice = products.stream()
                .map(Product::getPrice)
                .reduce(0, Integer::sum);

        return new Payment(
                generatedId,
                productIds,
                paidPrice,
                userId
        );
    }
}

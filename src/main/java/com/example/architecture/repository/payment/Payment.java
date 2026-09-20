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
    private PaymentStatus status = PaymentStatus.IN_PAYMENT;
    private LocalDateTime purchasedAt;  // 결제 완료 시점
    private LocalDateTime deliveredAt;  // 배송 완료 시점
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

    // 구매 완료
    public void complete(Integer requestUserId) {
        if (!requestUserId.equals(super.getCreatedBy())) {
            throw new RuntimeException("취소하려는 유저와 취소하려는 결제를 수행한 유저가 다릅니다.");
        }
        if (this.status.compareTo(PaymentStatus.PAYMENT_COMPLETE) > 0) {
            throw new RuntimeException("결제 완료로 상태를 바꿀 수 없는 결제 건입니다.");
        }
        this.status = PaymentStatus.PAYMENT_COMPLETE;
        this.purchasedAt = LocalDateTime.now();
        super.updated(requestUserId);
    }

    // 구매 취소
    public void cancel(Integer requestedUserId) {
        if (!requestedUserId.equals(super.createdBy)) {
            throw new RuntimeException("취소하려는 유저와 취소하려는 결제를 수행한 유저가 다릅니다.");
        }
        if (!this.status.isCancellable()) {
            throw new RuntimeException("취소하시려는 결제는 취소할 수 없는 상태입니다.");
        }
        this.status = PaymentStatus.CANCEL_COMPLETE;
        this.cancelledAt = LocalDateTime.now();
        super.updated(requestedUserId);
    }
}

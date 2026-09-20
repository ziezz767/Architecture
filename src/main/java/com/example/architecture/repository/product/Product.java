package com.example.architecture.repository.product;

import com.example.architecture.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class Product extends BaseEntity {
    private static int PRODUCT_CURRENT_ID = 0;
    private static int idGenerator() {
        return ++PRODUCT_CURRENT_ID;
    }

    private String name;
    private int price;
    private int stock;

    private Product(Integer id, String name, int price, int stock, Integer userId) {
        super(id, userId);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // userId => 누가 상품을 생성했는지
    public static Product create(String name, int price, int stock, Integer userId) {
        int generatedId = idGenerator();
        return new Product(
                generatedId,
                name,
                price,
                stock,
                userId
        );
    }

    // 상품 구매 가능 여부
    public void buyable() {
        if (this.stock < 1) {
            throw new RuntimeException("구매하시려는 상품의 재고가 존재하지 않습니다.");
        }
    }

    // 상품 재고 감소
    public void decrease() {
        this.stock -= 1;
    }

    // 상품 재고 증가
    public void increase() {
        this.stock += 1;
    }

}

package com.example.architecture.repository.product;

import com.example.architecture.repository.BaseEntity;
import lombok.Getter;

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
}

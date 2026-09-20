package com.example.architecture.controller.internal.api.dto;


import com.example.architecture.repository.product.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentCreateRequestDto extends RequestingUserDto{
    private List<Integer> productIds;

    public PaymentCreateRequestDto(List<Integer> productIds, Integer requestUserId) {
        super(requestUserId);
        this.productIds = productIds;
    }
}

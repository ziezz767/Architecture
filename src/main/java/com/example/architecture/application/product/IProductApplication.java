package com.example.architecture.application.product;

import com.example.architecture.controller.internal.api.dto.ProductResponseDto;

import java.util.List;

public interface IProductApplication {
    // 전체 조회
    List<ProductResponseDto> retrieve();

    // 상세 조회
    ProductResponseDto retrieve(Integer id);
}

package com.example.architecture.application;

import com.example.architecture.controller.internal.api.dto.ProductResponseDto;
import com.example.architecture.repository.product.Product;
import com.example.architecture.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductApplication {
    private final ProductService productService;

    // 전체 조회
    public List<ProductResponseDto> retrieve() {
        List<Product> products = productService.getProducts();
        return products.stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    // 상세 조회
    public ProductResponseDto retrieve(Integer id) {
        Product retrieved = productService.getProduct(id);
        return ProductResponseDto.from(retrieved);
    }
}

package com.example.architecture.service.product;

import com.example.architecture.controller.internal.api.dto.ProductResponseDto;
import com.example.architecture.repository.product.Product;
import com.example.architecture.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 전체 조회
    public List<ProductResponseDto> retrieve() {
        List<Product> entity = productRepository.findAll();
        return entity.stream()
                .map(ProductResponseDto::from)
                .toList();
    }

    // 상세 조회
    public ProductResponseDto retrieve(Integer id) {
        Optional<Product> WrappedProduct = productRepository.findById(id);
                 Product         product = WrappedProduct
                .orElseThrow(() -> new RuntimeException("찾으시는 유저가 존재하지 않습니다."));
        return ProductResponseDto.from(product);
    }
}

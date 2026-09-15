package com.example.architecture.controller.internal;

import com.example.architecture.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/*
    < 실제 고객의 구매와 구매취소 기능 / 버튼에 대한 API 제공 >
    - 고객이 쿠팡에 어떤 물건들이 있는지 확인할 수 있는 전체 상품 조회
    - 고객이 특정 상품에 대한 상세 정보를 볼 수 있게 할 단일 상품 조회
*/

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
}

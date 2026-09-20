package com.example.architecture.controller.internal.api;

import com.example.architecture.application.ProductApplication;
import com.example.architecture.controller.internal.api.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/*
    < 실제 고객의 구매와 구매취소 기능 / 버튼에 대한 API 제공 >
    - 고객이 쿠팡에 어떤 물건들이 있는지 확인할 수 있는 전체 상품 조회
    - 고객이 특정 상품에 대한 상세 정보를 볼 수 있게 할 단일 상품 조회
*/

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductApplication productApplication;

    // 전체 조회
    @RequestMapping(method = RequestMethod.GET, value = "/internal/api/products")
    public List<ProductResponseDto> retrieve() {
        return productApplication.retrieve();
    }

    // 상세 조회
    @RequestMapping(method = RequestMethod.GET, value = "/internal/api/products/{id}")
    public ProductResponseDto retrieve(@PathVariable Integer id) {
        return productApplication.retrieve(id);
    }
}

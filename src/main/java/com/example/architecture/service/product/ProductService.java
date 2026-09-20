package com.example.architecture.service.product;

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

    // findAll
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // findById
    public Product getProduct(Integer productId) {
        Optional<Product> WrappedProduct = productRepository.findById(productId);
                 Product         product = WrappedProduct
                .orElseThrow(() -> new RuntimeException("결제한 상품이 존재하지 않습니다."));
        return product;
    }

    // findById (Optional)
    public Optional<Product> findProduct(Integer id) {
        return productRepository.findById(id);
    }

    // update
    public Product update(Product entity) {
        Optional<Product> WrappedProduct = productRepository.update(entity);
                 Product         product = WrappedProduct
                .orElseThrow(() -> new RuntimeException("업데이트가 정상적으로 되지 않습니다."));
        return product;
    }

}

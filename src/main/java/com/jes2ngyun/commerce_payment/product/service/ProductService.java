package com.jes2ngyun.commerce_payment.product.service;

import com.jes2ngyun.commerce_payment.common.dto.PageResponse;
import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import com.jes2ngyun.commerce_payment.product.dto.request.ProductSearchCondition;
import com.jes2ngyun.commerce_payment.product.dto.response.ProductDetailResponse;
import com.jes2ngyun.commerce_payment.product.dto.response.ProductResponse;
import com.jes2ngyun.commerce_payment.product.entity.Product;
import com.jes2ngyun.commerce_payment.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 목록 조회 (동적 검색 + 페이징)
    public PageResponse<ProductResponse> getProducts(ProductSearchCondition condition, Pageable pageable) {

        Page<ProductResponse> page = productRepository.search(condition, pageable)
                .map(ProductResponse::from);

        return PageResponse.from(page);
    }

    // 상품 단건 조회
    public ProductDetailResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.from(product);
    }
}
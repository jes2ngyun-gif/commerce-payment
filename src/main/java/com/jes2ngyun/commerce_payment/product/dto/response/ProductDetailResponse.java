package com.jes2ngyun.commerce_payment.product.dto.response;

import com.jes2ngyun.commerce_payment.product.entity.Category;
import com.jes2ngyun.commerce_payment.product.entity.Product;

import java.time.LocalDateTime;

public record ProductDetailResponse(
        Long id,
        String name,
        int price,
        int stockQuantity,
        Category category,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

// ProductDetailResponse 클래스는 단건조회용
// 목록과 상세를 분리한 이유는 목록에서 상품 100개를 내려줄 때 description까지 다 실으면 응답이 비대해짐.
// 화면에 필요한 만큼만 주는 것이 API 설계의 기본임.
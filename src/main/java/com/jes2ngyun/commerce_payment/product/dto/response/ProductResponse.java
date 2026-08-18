package com.jes2ngyun.commerce_payment.product.dto.response;

import com.jes2ngyun.commerce_payment.product.entity.Category;
import com.jes2ngyun.commerce_payment.product.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int stockQuantity,
        Category category
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
        );
    }
}
// 엔티티는 stock, DTO는 stockQuantity. 어긋난 거 아니고 의도한 것.
// DTO의 존재 이유가 이거다. 내부(DB) 이름과 외부(API) 이름을 따로 가져갈 수 있어서, 나중에 DB 컬럼명을 바꿔도 API 스펙은 그대로 유지됌.

// ProductResponse 클래스는 목록용.
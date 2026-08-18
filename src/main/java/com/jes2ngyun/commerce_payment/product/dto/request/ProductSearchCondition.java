package com.jes2ngyun.commerce_payment.product.dto.request;

import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import com.jes2ngyun.commerce_payment.product.entity.Category;

public record ProductSearchCondition(
        Category category,
        Integer minPrice,
        Integer maxPrice
) {
    public ProductSearchCondition {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BusinessException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }
    }
}
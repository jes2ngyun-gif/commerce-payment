package com.jes2ngyun.commerce_payment.product.repository;

import com.jes2ngyun.commerce_payment.product.dto.request.ProductSearchCondition;
import com.jes2ngyun.commerce_payment.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> search(ProductSearchCondition condition, Pageable pageable);
}
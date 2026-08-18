package com.jes2ngyun.commerce_payment.product.controller;

import com.jes2ngyun.commerce_payment.common.dto.PageResponse;
import com.jes2ngyun.commerce_payment.product.dto.request.ProductSearchCondition;
import com.jes2ngyun.commerce_payment.product.dto.response.ProductDetailResponse;
import com.jes2ngyun.commerce_payment.product.dto.response.ProductResponse;
import com.jes2ngyun.commerce_payment.product.entity.Category;
import com.jes2ngyun.commerce_payment.product.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) @Min(0) Integer minPrice,
            @RequestParam(required = false) @Min(0) Integer maxPrice,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        ProductSearchCondition condition = new ProductSearchCondition(category, minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(productService.getProducts(condition, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
}
package com.jes2ngyun.commerce_payment.product.repository;

import com.jes2ngyun.commerce_payment.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
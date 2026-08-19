package com.jes2ngyun.commerce_payment.cart.repository;

import com.jes2ngyun.commerce_payment.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
}
// findByCartIdAndProductId -> 이 장바구니에 이 상품이 이미 있나?? 를 확인하는 용도. 수량 합산 로직의 핵심
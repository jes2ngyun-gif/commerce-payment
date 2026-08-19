package com.jes2ngyun.commerce_payment.cart.repository;

import com.jes2ngyun.commerce_payment.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember_Id(Long memberId);
}
// Cart에 memberId 필드는 없고 member 객체만 있음.
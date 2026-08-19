package com.jes2ngyun.commerce_payment.cart.entity;

import com.jes2ngyun.commerce_payment.common.entity.BaseTimeEntity;
import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import com.jes2ngyun.commerce_payment.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cart_product",
                columnNames = {"cart_id", "product_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Builder
    private CartItem(Product product, int quantity) {
        validateQuantity(quantity);
        validateStock(product, quantity);

        this.product = product;
        this.quantity = quantity;
    }

    // ────────── 비즈니스 메서드 ──────────

    // 양방향 연관관계 설정 — Cart.addCartItem 에서만 호출
    void assignCart(Cart cart) {
        this.cart = cart;
    }

    // 같은 상품을 또 담았을 때 — 수량 합산
    public void addQuantity(int quantity) {
        validateQuantity(quantity);

        int newQuantity = this.quantity + quantity;
        validateStock(this.product, newQuantity);

        this.quantity = newQuantity;
    }

    // 수량 변경 — 덮어쓰기
    public void changeQuantity(int quantity) {
        validateQuantity(quantity);
        validateStock(this.product, quantity);

        this.quantity = quantity;
    }

    // 이 항목의 금액 = 상품 가격 × 수량
    public int getTotalPrice() {
        return this.product.getPrice() * this.quantity;
    }

    // ────────── 검증 ──────────

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new BusinessException(ErrorCode.INVALID_CART_QUANTITY);
        }
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new BusinessException(ErrorCode.INVALID_CART_QUANTITY);
        }
    }
}
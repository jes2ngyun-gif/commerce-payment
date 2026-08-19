package com.jes2ngyun.commerce_payment.cart.entity;

import com.jes2ngyun.commerce_payment.common.entity.BaseTimeEntity;
import com.jes2ngyun.commerce_payment.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Builder
    private Cart(Member member) {
        this.member = member;
    }

    // ────────── 비즈니스 메서드 ──────────

    // 장바구니에 상품 추가 — 양방향 연관관계를 함께 설정한다
    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.assignCart(this);
    }

    // 장바구니 항목 제거 — 양쪽 관계를 모두 끊는다 (orphanRemoval 로 DB 삭제)
    public void removeCartItem(CartItem cartItem) {
        if (this.cartItems.remove(cartItem)) {
            cartItem.assignCart(null);
        }
    }

    // 장바구니 전체 비우기 — 양쪽 관계를 모두 끊는다
    public void clear() {
        this.cartItems.forEach(cartItem -> cartItem.assignCart(null));
        this.cartItems.clear();
    }

    // 합계 금액 = 모든 항목 금액의 합
    public int getTotalAmount() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    // 소유권 검증
    public boolean isOwnedBy(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}
package com.jes2ngyun.commerce_payment.product.entity;

import com.jes2ngyun.commerce_payment.common.entity.BaseTimeEntity;
import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 50)
    private Category category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Builder
    private Product(String name, Category category, int price, int stock, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // ────────── 비즈니스 메서드 ──────────

    // 재고 차감 — 주문 생성 시 사용
    public void decreaseStock(int quantity) {
        validateQuantity(quantity);

        if (this.stock < quantity) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }
        this.stock -= quantity;
    }

    // 재고 복구 — 결제 실패 / 주문 취소 시 사용
    public void increaseStock(int quantity) {
        validateQuantity(quantity);

        this.stock += quantity;
    }

    // 수량은 항상 1 이상이어야 한다.
    // 이 검증에 걸린다는 것은 사용자 입력 문제가 아니라 호출하는 코드의 버그다.
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "재고 변경 수량은 1 이상이어야 합니다. 요청 수량 = " + quantity
            );
        }
    }
}

//✅ 검증이 객체 안에 갇혀 있음
//product.decreaseStock(quantity);
// Product 객체는 자기 재고를 스스로 지킨다. 누가 어디서 호출하든 음수가 될 수 없음.
// increaseStock에는 왜 검증이 없을까? 재고를 늘리는 건 실패할 이유가 없기 때문.

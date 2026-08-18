package com.jes2ngyun.commerce_payment.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

// BusinessException - 내가 던질 예외
// RuntimeException을 상속한 이유?? - 제일 중요함.
// Spring의 @Transaction은 기본적으로 RuntimeException(unchecked)일 때만 롤백한다. Exception(checked)을 상속하면 롤백이 안 됌.
//무슨 뜻이냐~~ — 5단계 주문 생성을 떠올려보자.
//
//java
//@Transactional
//public Order createOrder(...) {
//    product.decreaseStock(2);                              // ① 재고 10 → 8
//    if (다른상품재고부족) {
//        throw new BusinessException(OUT_OF_STOCK);          // ② 예외!
//    }
//}
//
//RuntimeException 계열이면 ①의 재고 차감이 롤백돼서 10으로 돌아온다.
//Exception 계열이면 재고가 8인 채로 남는다. 주문은 실패했는데 재고만 사라짐. 이게 실무에서 말하는 데이터 정합성이 깨진 상태.
//
//기본기 가이드의 "자주 막히는 지점"에 이게 그대로 나와 있다.
// extends RuntimeException — 이 한 단어가 그 사고를 막는다.
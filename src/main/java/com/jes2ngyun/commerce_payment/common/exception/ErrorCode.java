package com.jes2ngyun.commerce_payment.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 공통 ─────────────────────────────
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 형식입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // ── 인증 / 인가 ───────────────────────
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),        // ← 이 줄

    // ── 회원 ─────────────────────────────
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),

    // ── 상품 ─────────────────────────────
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_SEARCH(HttpStatus.BAD_REQUEST, "상품 검색 조건이 올바르지 않습니다."),

    // ── 장바구니 ──────────────────────────
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 상품을 찾을 수 없습니다."),
    INVALID_CART_QUANTITY(HttpStatus.BAD_REQUEST, "상품 수량이 올바르지 않습니다."),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 장바구니에 접근할 권한이 없습니다."),

    // ── 주문 ─────────────────────────────
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),
    ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 주문에 접근할 권한이 없습니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),
    INVALID_ORDER_STATUS(HttpStatus.CONFLICT, "결제 가능한 주문 상태가 아닙니다."),
    CANCEL_NOT_ALLOWED(HttpStatus.CONFLICT, "이미 결제가 완료되었거나 취소된 주문은 처리할 수 없습니다."),

    // ── 결제 ─────────────────────────────
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 결제를 취소할 권한이 없습니다."),
    INVALID_PAYMENT_STATUS(HttpStatus.CONFLICT, "결제 가능한 결제 상태가 아닙니다."),
    PAYMENT_CANCEL_NOT_ALLOWED(HttpStatus.CONFLICT, "현재 상태에서는 결제를 취소할 수 없습니다."),
    PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 금액과 결제 금액이 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
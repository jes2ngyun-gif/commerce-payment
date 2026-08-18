package com.jes2ngyun.commerce_payment.common.security;

public record CustomUserPrincipal(
        Long memberId,
        String email,
        String role
) {}

// 현재 로그인한 회원을 담는 그릇
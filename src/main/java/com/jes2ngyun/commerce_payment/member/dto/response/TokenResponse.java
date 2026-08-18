package com.jes2ngyun.commerce_payment.member.dto.response;

public record TokenResponse(String tokenType, String accessToken) {

    public static TokenResponse of(String accessToken) {
        return new TokenResponse("Bearer", accessToken);
    }
}

// Bearer는 이 토큰을 지참한 사람에게 권한을 준다는 뜻

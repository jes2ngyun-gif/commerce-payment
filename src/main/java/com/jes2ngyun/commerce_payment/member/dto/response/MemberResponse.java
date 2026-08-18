package com.jes2ngyun.commerce_payment.member.dto.response;

import com.jes2ngyun.commerce_payment.member.entity.Member;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String phone,
        LocalDateTime createdAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getCreatedAt()
        );
    }
}
// 엔티티를 그대로 반환하면 안되는 이유 3가지
// 1. 비번이 응답에 나감. Member엔 password필드가 있다. JSON으로 변환되면서 그대로 노출된다. 명세서에도 비번은 응답에 포함ㄴ라고 되어 있음.
// 2. LazyInitializationException이 터진다. 나중에 Order처럼 연관관계가 있는 엔티티를 반환하면 터짐
// 3. 엔티티 필드명을 바꾸는 순간 API 스펙이 깨짐.

// from() 정적 메서드로 변환 책임을 DTO가 갖게 했다. -> 서비스 코드가 깔끔해짐.

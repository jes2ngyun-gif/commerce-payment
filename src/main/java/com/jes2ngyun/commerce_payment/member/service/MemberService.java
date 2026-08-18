package com.jes2ngyun.commerce_payment.member.service;

import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import com.jes2ngyun.commerce_payment.member.dto.request.SignupRequest;
import com.jes2ngyun.commerce_payment.member.dto.response.MemberResponse;
import com.jes2ngyun.commerce_payment.member.entity.Member;
import com.jes2ngyun.commerce_payment.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor       // final 필드 받는 생성자를 자동 생성함. 생성자가 하나뿐이면 스프링이 알아서 의존성 주입해줌.
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse signup(SignupRequest request) {

        // 1. 이메일 중복 검사
        if (memberRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 2. 비밀번호 암호화 후 회원 생성
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .phone(request.phone())
                .build();

        // 3. 저장 후 DTO로 변환하여 반환
        return MemberResponse.from(memberRepository.save(member));
    }
}
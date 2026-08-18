package com.jes2ngyun.commerce_payment.member.service;

import com.jes2ngyun.commerce_payment.common.exception.BusinessException;
import com.jes2ngyun.commerce_payment.common.exception.ErrorCode;
import com.jes2ngyun.commerce_payment.common.security.JwtProvider;
import com.jes2ngyun.commerce_payment.member.dto.request.LoginRequest;
import com.jes2ngyun.commerce_payment.member.dto.request.SignupRequest;
import com.jes2ngyun.commerce_payment.member.dto.response.MemberResponse;
import com.jes2ngyun.commerce_payment.member.dto.response.TokenResponse;
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
    private final JwtProvider jwtProvider;

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

    public TokenResponse login(LoginRequest request) {

        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        // 2. 비밀번호 대조
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. 토큰 발급
        String accessToken = jwtProvider.createToken(
                member.getId(),
                member.getEmail(),
                member.getRole().name()
        );

        return TokenResponse.of(accessToken);
    }

    public MemberResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}
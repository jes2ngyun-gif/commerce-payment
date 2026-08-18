package com.jes2ngyun.commerce_payment.member.controller;

import com.jes2ngyun.commerce_payment.common.security.CustomUserPrincipal;
import com.jes2ngyun.commerce_payment.member.dto.response.MemberResponse;
import com.jes2ngyun.commerce_payment.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        return ResponseEntity.ok(memberService.getMyInfo(principal.memberId()));
    }
}
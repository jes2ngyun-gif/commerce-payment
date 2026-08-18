package com.jes2ngyun.commerce_payment.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {

            CustomUserPrincipal principal = new CustomUserPrincipal(
                    jwtProvider.getMemberId(token),
                    jwtProvider.getEmail(token),
                    jwtProvider.getRole(token)
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + principal.role()))
            );

            // 공식 권장 방식: 새 컨텍스트를 만들어 넣는다 (스레드 간 경합 방지)
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    // "Bearer {토큰}" 에서 토큰만 잘라낸다
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}

// 1. 토큰이 없어도 예외를 안 던지고 그냥 통과시킨다.
// if (token != null && ...) ~ filterChain.doFilter(request, reponse);
// 이 필터의 역할은 "차단"이 아니라 "신원 확인 시도"임. 확인이 되면 도장을 찍고, 안 되면 그냥 빈 손으로 보냄.
// 차단 여부는 AuthorizationFilter가 경로별로 판단함.

// 2. SecurityContextHolder는 ThreadLocal이다.
// SecurityContextHolder.getContext().setAuthentication(authentication);
//
//여기에 저장하면 같은 요청을 처리하는 동안 어디서든 꺼내 쓸 수 있다. 컨트롤러의 @AuthentiationPrincipal도 결국 여기서 꺼내옴.

// 3. "ROLE_" 접두사는 Spring Security의 규칙

// 4. OncePerRequestFilter를 상속하는 이유??
// 이름 그대로 요청당 정확히 한 번만 실행되게 보장한다.

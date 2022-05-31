package com.basic.board.domain.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Value("${filter.skip.paths}")
    private List<String> skipPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("filter 실행");

        //0. skipPath 에 등록된 uri 인 경우 통과
        String uri = request.getRequestURI();
        if (skipPath.contains(uri)) {
            System.out.println("통과 " + uri);
            filterChain.doFilter(request, response);
            return;
        }

        //1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken(request);

        //2. validateToken 으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            //Redis 에 해당 accessToken logout 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);
            if (ObjectUtils.isEmpty(isLogout)) {
                //토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //3. 토큰이 없거나 잘못된 토큰인 경우
        else {
            System.out.println("실패 " + request.getRequestURI());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/api/auth/fail");
            request.setAttribute("msg", "Unauthorized");
            requestDispatcher.forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    //Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            //'Bearer ' 공백까지 총 7자리
            return bearerToken.substring(7);
        }
        return null;
    }
}

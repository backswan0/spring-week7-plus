package org.example.expert.common.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.enums.UserRole;
import org.example.expert.common.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;  // JwtUtil을 주입받아 JWT 토큰 처리

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String url = request.getRequestURI();  // 요청 URL을 가져옴

        if (url.startsWith("/auth")) {
            // "/auth"로 시작하는 URL은 JWT 검증을 건너뛰고, 필터 체인에 전달
            filterChain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");  // 요청 헤더에서 JWT 토큰을 가져옴

        if (bearerJwt == null) {
            // JWT 토큰이 없으면 400 Bad Request 반환
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);  // "Bearer " 접두어를 제거하고 JWT만 추출
        log.info("jwt: {}", jwt);

        try {
            // JWT의 유효성을 검사하고 claims(페이로드) 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                // JWT가 유효하지 않으면 400 Bad Request 반환
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            // JWT에서 userRole을 추출하고, 해당 값을 UserRole enum으로 변환
            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            // JWT에서 사용자 정보를 추출하여 request 객체에 세팅
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));
            request.setAttribute("nickname", claims.get("nickname"));

            log.info("인증 객체 생성 전");

            // 인증된 사용자 객체를 생성하여 SecurityContext에 설정
            User user = new User(
                claims.getSubject(), // JWT (JSON Web Token)에서 subject 정보 추출
                "",  // 패스워드는 비워두고
                List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()))  // 사용자 권한 설정
            );

            log.info("인증 객체 설정 시작");
            SecurityContextHolder.getContext()
                .setAuthentication(  // Spring Security의 인증 정보 설정
                    new UsernamePasswordAuthenticationToken(
                        user,  // 사용자 정보
                        null,  // 패스워드는 비워두고
                        user.getAuthorities()  // 권한 설정
                    )
                );
            log.info("인증 객체 설정 완료");

            filterChain.doFilter(request, response);  // 필터 체인으로 계속 진행

        } catch (SecurityException | MalformedJwtException e) {
            // JWT 서명이 잘못되었거나 형식이 잘못되면 401 Unauthorized 반환
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            // JWT가 만료되었으면 401 Unauthorized 반환
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 JWT 토큰이면 400 Bad Request 반환
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            // 기타 예외는 500 Internal Server Error 반환
            log.error("Internal server error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
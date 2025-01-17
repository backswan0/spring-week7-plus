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

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String url = request.getRequestURI();

        if (url.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "JWT 토큰이 필요합니다."
            );
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);
        log.info("jwt: {}", jwt);

        try {
            // JWT의 유효성을 검사하고 claims(페이로드) 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "잘못된 JWT 토큰입니다."
                );
                return;
            }

            UserRole userRole = UserRole.valueOf(
                claims.get("userRole", String.class)
            );

            String nickname = claims.get("nickname", String.class);

            log.info("인증 객체 생성 전");

            User user = new User(
                claims.getSubject(),
                "",
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + userRole.name()
                    )
                )
            );

            log.info("인증 객체 설정 시작");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
            );
            authenticationToken.setDetails(nickname);

            log.info("인증 객체 설정 완료");

            log.info("SecurityContext에 인증 정보 설정 시작");

            SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

            log.info("SecurityContext에 인증 정보 설정 완료");

        } catch (SecurityException | MalformedJwtException e) {
            //
            log.error("Invalid JWT signature", e);
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "유효하지 않는 JWT 서명입니다."
            );
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "만료된 JWT 토큰입니다."
            );
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "지원되지 않는 JWT 토큰입니다."
            );
        } catch (Exception e) {
            log.error("Internal server error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
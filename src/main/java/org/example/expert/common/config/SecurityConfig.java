package org.example.expert.common.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // Spring Security 설정 활성화
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)  // 메서드 수준의 보안 활성화 @Secured 어노테이션을 사용하여 메서드 권한을 설정 가능
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean  // SecurityFilterChain Bean을 등록하여 보안 설정을 처리
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화, 즉 기본 인증 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 인증 비활성화

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/sign-up").permitAll() // 회원 가입 API는 인증 없이 접근 가능
                .requestMatchers("/auth/sign-in").permitAll() // 로그인 API는 인증 없이 접근 가능
                .requestMatchers("/admin/users/**")
                .hasRole("ADMIN") // "/admin/users/**" 경로는 ADMIN 권한을 가진 사용자만 접근 가능
                .anyRequest().authenticated()  // 나머지 모든 요청은 인증된 사용자만 접근 가능
            )
            .build(); // 보안 필터 체인을 설정한 후 반환
    }
}
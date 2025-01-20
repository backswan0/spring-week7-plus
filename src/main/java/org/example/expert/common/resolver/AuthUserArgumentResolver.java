package org.example.expert.common.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.enums.UserRole;
import org.example.expert.common.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;

        boolean isTypeAuthUserDto = parameter.getParameterType()
            .equals(AuthUserDto.class);

        // @Auth 어노테이션과 AuthUserDto 타입이 같이 사용되지 않으면 예외 발생
        if (hasAuthAnnotation != isTypeAuthUserDto) {
            throw new AuthException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }

        // @Auth 어노테이션이 있으면 true, 아니면 false 반환
        return hasAuthAnnotation;
    }

    // 파라미터를 해석하여 반환하는 기능
    @Override
    public Object resolveArgument(
        @Nullable MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthException("사용자 인증 정보가 없습니다.");
        }

        User user = (User) authentication.getPrincipal();
        Long userId = Long.parseLong(authentication.getName());
        String email = (String) authentication.getCredentials();

        log.info("email: {}", email);

        String nickname = (String) authentication.getDetails();

        UserRole userRole = UserRole.valueOf(
            user.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "")
        );

        return new AuthUserDto(
            userId,
            email,
            userRole,
            nickname
        );
    }
}
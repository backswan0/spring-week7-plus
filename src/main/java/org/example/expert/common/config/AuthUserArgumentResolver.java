package org.example.expert.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.enums.UserRole;
import org.example.expert.common.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// HandlerMethodArgumentResolver 인터페이스를 구현하여 @Auth 어노테이션 처리
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    // 매개변수가 AuthUserDto 타입이며 @Auth 어노테이션을 가졌는지 검증
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터에 @Auth 어노테이션이 있는지 확인
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;

        // 파라미터 타입이 AuthUserDto인지 확인
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUserDto.class);

        // @Auth 어노테이션과 AuthUserDto 타입이 같이 사용되지 않으면 예외 발생
        if (hasAuthAnnotation != isAuthUserType) {
            throw new AuthException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }

        // @Auth 어노테이션이 있으면 true 반환, 아니면 false
        return hasAuthAnnotation;
    }

    // 파라미터를 해석하여 반환하는 기능
    @Override
    public Object resolveArgument(
        @Nullable MethodParameter parameter,  // 메서드 파라미터 정보
        @Nullable ModelAndViewContainer mavContainer,  // 모델과 뷰 컨테이너 정보
        NativeWebRequest webRequest,  // 웹 요청 정보
        @Nullable WebDataBinderFactory binderFactory  // 데이터 바인딩 관련 팩토리 정보
    ) {
        // HttpServletRequest를 통해 실제 HTTP 요청 정보를 가져옴
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // JwtFilter에서 설정한 사용자 정보를 HttpServletRequest에서 가져옴
        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("email");
        UserRole userRole = UserRole.of((String) request.getAttribute("userRole"));
        String nickname = (String) request.getAttribute("nickname");

        // 가져온 정보를 이용해 AuthUserDto 객체를 생성하여 반환
        return new AuthUserDto(
            userId,  // 사용자 ID
            email,  // 사용자 이메일
            userRole,  // 사용자 역할 (Enum 타입)
            nickname  // 사용자 닉네임
        );
    }
}
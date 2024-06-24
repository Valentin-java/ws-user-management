package com.workers.wsusermanagement.config.security;

import com.workers.wsusermanagement.config.security.context.TokenAuthenticationFilterContext;
import com.workers.wsusermanagement.util.SecurityValidationUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static com.workers.wsusermanagement.util.Constants.AUTH_TOKEN_PREFIX;
import static com.workers.wsusermanagement.util.Constants.UNEXPECTED_TEXT_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityValidationUtil securityUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        log.debug("[doFilterInternal] Start method");
        Optional.of(createContextFilter(request, response, filterChain))
                .map(this::validateHeader)
                .map(this::tokenContainUsername)
                .map(this::validateTokenExpiration)
                .map(this::setAuthenticationContext)
                .map(this::continueFilterChain)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, UNEXPECTED_TEXT_ERROR));
    }
    
    private TokenAuthenticationFilterContext createContextFilter(HttpServletRequest request,
                                                                 HttpServletResponse response,
                                                                 FilterChain filterChain) {
        return new TokenAuthenticationFilterContext(request, response, filterChain);
    }

    private TokenAuthenticationFilterContext validateHeader(TokenAuthenticationFilterContext context) {
        String header = securityUtil.getHeaderRequest(context);
        if (Strings.isEmpty(header)
                || !header.startsWith(AUTH_TOKEN_PREFIX)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Отсуствует, либо заголовок, либо токен");
        }
        return context;
    }

    private TokenAuthenticationFilterContext tokenContainUsername(TokenAuthenticationFilterContext context) {
        String user = securityUtil.getUsername(context);

        if (user == null) {
            log.error("Token provided but not content data!");
            throw new ResponseStatusException(UNAUTHORIZED, "В токене отсуствует имя пользователя");
        }
        return context;
    }

    // Валидация токена на время жизни
    private TokenAuthenticationFilterContext validateTokenExpiration(TokenAuthenticationFilterContext context) {
        String token = securityUtil.getToken(context);

        Claims claims = securityUtil.extractAllClaims(token);
        if (securityUtil.isTokenExpired(claims)) {
            log.error("[validateTokenExpiration] Время жизни токена истекло.");
            throw new ResponseStatusException(UNAUTHORIZED, "Время жизни токена истекло");
        }
        return context;
    }

    private TokenAuthenticationFilterContext setAuthenticationContext(TokenAuthenticationFilterContext context) {
        String userId = securityUtil.extractClaimByName(securityUtil.getToken(context), "userId");

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return context;
    }

    private TokenAuthenticationFilterContext continueFilterChain(TokenAuthenticationFilterContext context) {
        try {
            context.getFilterChain().doFilter(context.getRequest(), context.getResponse());
        } catch (IOException | ServletException e) {
            throw new ResponseStatusException(UNAUTHORIZED, UNEXPECTED_TEXT_ERROR);
        }
        return context;
    }
}

package com.workers.wsusermanagement.config.feign.interceptor;

import com.workers.wsusermanagement.config.feign.service.AuthService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TokenInterceptorConfiguration implements RequestInterceptor {

    private final AuthService authService;

    @Override
    public void apply(RequestTemplate template) {
        String token = authService.getToken();
        if (token != null) {
            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
    }
}

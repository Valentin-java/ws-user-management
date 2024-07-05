package com.workers.wsusermanagement.config.resttemplate.service;

import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthRequest;
import com.workers.wsusermanagement.rest.outbound.feign.dto.AuthResponse;
import com.workers.wsusermanagement.config.resttemplate.properties.CredentialProps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Date;

import static com.workers.wsusermanagement.config.security.util.CommonUtil.getPublicKeyFromPem;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final CredentialProps credentialProps;
    private String cachedToken;
    @Value("${jwt.public.key}")
    private String publicKeyString;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = getPublicKeyFromPem(publicKeyString);
    }

    public String getToken() {
        if (isTokenValid(cachedToken)) {
            return cachedToken;
        }

        cachedToken = fetchNewToken();
        return cachedToken;
    }

    private String fetchNewToken() {
        var request = new AuthRequest(credentialProps.getUsername(), credentialProps.getPassword());
        var response = restTemplate.postForObject(credentialProps.getAuthUrl(), request, AuthResponse.class);
        return response.accessToken();
    }

    private boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        return !extractAllClaims(token).getExpiration().before(new Date());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

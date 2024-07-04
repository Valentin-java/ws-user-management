package com.workers.wsusermanagement.config.feign.service;

import com.workers.wsusermanagement.config.feign.dto.AuthRequest;
import com.workers.wsusermanagement.config.feign.dto.AuthResponse;
import com.workers.wsusermanagement.config.feign.properties.CredentialProps;
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
    @Value("${jwt.public.key}")
    private String publicKeyString;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = getPublicKeyFromPem(publicKeyString);
    }

    public String getToken() {
        String token = getTokenFromSecurityContext();
        if (isTokenValid(token)) {
            return token;
        }

        token = fetchNewToken();
        updateSecurityContextWithNewToken(token);
        return token;
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


    private String getTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof String) {
            return (String) authentication.getDetails();
        }
        return null;
    }

    private void updateSecurityContextWithNewToken(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof AbstractAuthenticationToken) {
            ((AbstractAuthenticationToken) authentication).setDetails(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}

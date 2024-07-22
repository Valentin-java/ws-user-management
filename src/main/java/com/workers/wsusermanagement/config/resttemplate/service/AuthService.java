package com.workers.wsusermanagement.config.resttemplate.service;

import com.workers.wsusermanagement.config.resttemplate.properties.CredentialProps;
import com.workers.wsusermanagement.rest.outbound.model.AuthRequest;
import com.workers.wsusermanagement.rest.outbound.model.AuthResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.security.PublicKey;
import java.util.Date;

import static com.workers.wsusermanagement.config.security.util.CommonUtil.getPublicKeyFromPem;
import static com.workers.wsusermanagement.rest.outbound.util.CommonFeignUtil.getSpecificMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        try {
            var request = new AuthRequest(credentialProps.getUsername(), credentialProps.getPassword());
            var response = restTemplate.postForObject(credentialProps.getAuthUrl(), request, AuthResponse.class);
            return response.accessToken();
        } catch (Exception ex) {
        throw new ResponseStatusException(BAD_REQUEST, "Interceptor Service: " + getSpecificMessage(ex));
    }
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

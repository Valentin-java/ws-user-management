package com.workers.wsusermanagement.util;

import com.workers.wsusermanagement.config.security.context.TokenAuthenticationFilterContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.function.Function;

import static com.workers.wsusermanagement.util.Constants.AUTH_HEADER_NAME;
import static com.workers.wsusermanagement.util.Constants.AUTH_TOKEN_PREFIX;

@Component
public class SecurityValidationUtil {

    @Value("${jwt.public.key}")
    private String publicKeyString;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        publicKey = getPublicKeyFromPem(publicKeyString);
    }

    public Boolean isTokenExpired(Claims claims) {
        return !claims.getExpiration().before(new Date());
    }

    public String extractClaimByName(String token, String claimName) {
        return (String) extractAllClaims(token).get(claimName);
    }

    // Извлечение определенного параметра из токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Извлечение всех параметров из токена
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public PublicKey getPublicKeyFromPem(String publicKeyPEM) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid public key", e);
        }
    }

    public String getUsername(TokenAuthenticationFilterContext context) {
        return extractClaim(getToken(context), Claims::getSubject);
    }

    public String getToken(TokenAuthenticationFilterContext context) {
        String header = context.getRequest().getHeader(AUTH_HEADER_NAME);
        return header.replaceAll(AUTH_TOKEN_PREFIX, "");
    }

    public String getHeaderRequest(TokenAuthenticationFilterContext context) {
        return context.getRequest().getHeader(AUTH_HEADER_NAME);
    }
}

package br.com.viniciusacoelho.invoice_issuance_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.List;

public class JWTCreator {

    protected static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String ROLES_AUTHORITIES = "authorities";

    public static String create(String prefix, String key, JWTObject jwtObject) {
        String token = Jwts.builder()
                .subject(jwtObject.getSubject())
                .issuedAt(jwtObject.getIssuedAt())
                .expiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                .signWith(secretKey(key), Jwts.SIG.HS512)
                .compact();
        return prefix + " " + token;
    }

    public static JWTObject create(String token, String prefix, String key) {
        token = token.replace(prefix + " ", "");
        Claims claims = Jwts.parser()
                .verifyWith(secretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return JWTObject.builder()
                .subject(claims.getSubject())
                .expiration(claims.getExpiration())
                .issuedAt(claims.getIssuedAt())
                .roles(claims.get(ROLES_AUTHORITIES, List.class))
                .build();
    }

    private static SecretKey secretKey(String key) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    private static List<String> checkRoles(List<String> roles) {
        return roles.stream()
                .map(role -> "ROLE_".concat(role.replace("ROLE_", "")))
                .toList();
    }

}

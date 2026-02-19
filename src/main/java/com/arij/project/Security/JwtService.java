package com.arij.project.Security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    
    private static final String TOKEN_TYPE = "token_type" ;
    private final PrivateKey privateKey ;
    private final PublicKey publicKey ;
    @Value("${app.security.jwt.access-token-expiration}")
    private long accessTokenExpiration ;
    @Value("${app.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration ;

    public JwtService() throws Exception{
        this.privateKey = KeyUtils.loadPrivateKey("/keys/local-only/private_Key.pem") ;
        this.publicKey = KeyUtils.loadPublicKey("/keys/local-only/public_Key.pem") ; 

    }
    
    public String generateAccessToken(final String username) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, "ACCESS_TOKEN");
        return buildToken(username , claims,this.accessTokenExpiration);
    }
    
    public String generateRefreshToken (final String username) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE, "REFRESH_TOKEN");
        return buildToken(username , claims,this.refreshTokenExpiration);
    }
    private String buildToken(final String username, final Map<String, Object> claims, final long expiration) {
    return Jwts.builder()
               .setClaims(claims)                
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + expiration))
               .signWith(this.privateKey, SignatureAlgorithm.RS256)  
               .compact();
}
    public boolean isTokenValid(final String token , final String expectedUsername){
    final String username = extractUsername(token);
    return username.equals(expectedUsername)&& !isTokenExpired(token);

}
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration()
                                .before(new Date());
      
    }

    public String extractUsername (final String token ){
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(final String token) {
    try {
        // parseClaimsJws retourne un objet Jws<Claims>
        Jws<Claims> jws = Jwts.parserBuilder()
                              .setSigningKey(this.publicKey)  // clé publique pour vérifier
                              .build()
                              .parseClaimsJws(token);
        return jws.getBody();  // c'est un Claims, tu peux appeler getSubject()
    } catch (JwtException e) {
        throw new RuntimeException("Invalid JWT token", e);
    }
}
     public String refreshAccessToken(final String refreshToken){
        final Claims claims = extractClaims(refreshToken);
        if (!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))){
            throw new RuntimeException("Invalid token type");

        }
        if (isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token expired");

        }
        final String username = claims.getSubject();
        return generateAccessToken(username);
     }
    }



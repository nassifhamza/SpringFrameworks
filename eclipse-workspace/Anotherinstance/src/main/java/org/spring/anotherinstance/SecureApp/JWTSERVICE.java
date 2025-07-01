package org.spring.anotherinstance.SecureApp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTSERVICE {
private String secretKey="";
public JWTSERVICE() throws NoSuchAlgorithmException {
    KeyGenerator keyGen =KeyGenerator.getInstance("HmacSHA256");
    SecretKey SK=keyGen.generateKey();
   secretKey = Base64.getEncoder().encodeToString(SK.getEncoded());
}
    public String GenerateToken(String text) {
        Map<String,Object> claims = new LinkedHashMap<>();

        return Jwts.builder().claims().add(claims).subject(text).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 1000*60*60*24*5)).and().signWith(GetKey()).compact();
    }
   private SecretKey GetKey(){

byte[] keyBytes = Decoders.BASE64.decode(secretKey);
return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);

    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(GetKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String identifier = extractUserName(token); // Can be email or username
        if (userDetails instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) userDetails;
            return (identifier.equals(userPrincipal.getUsername()) || identifier.equals(userPrincipal.getEmail()))
                    && !isTokenExpired(token);
        }
        return false;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}

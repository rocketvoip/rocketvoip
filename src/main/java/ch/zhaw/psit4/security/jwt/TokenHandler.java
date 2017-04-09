package ch.zhaw.psit4.security.jwt;

import ch.zhaw.psit4.security.auxiliary.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author Rafael Ostertag
 */
public class TokenHandler {
    private final String secret;
    private final UserDetailsService userService;

    public TokenHandler(String secret, UserDetailsService userService) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.userService = userService;
    }

    public UserDetails parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userService.loadUserByUsername(username);
    }

    public String createTokenForUser(UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + SecurityConstants.TOKEN_EXPIRATION_IN_MILLIS);
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}

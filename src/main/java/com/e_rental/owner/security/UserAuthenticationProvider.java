package com.e_rental.owner.security;

import com.e_rental.owner.dto.request.LoginRequest;
import com.e_rental.owner.entities.Owner;
import com.e_rental.owner.enums.Role;
import com.e_rental.owner.repositories.OwnerRepository;
import com.e_rental.owner.utils.MessageSourceUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final OwnerRepository ownerRepository;

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(UserPrincipal userPrincipal) {
        Claims claims = Jwts.claims().setSubject(userPrincipal.getUsername());
        claims.put("role", userPrincipal.getAuthorities());
        claims.put("email", userPrincipal.getEmail());
        claims.put("username", userPrincipal.getUsername());

        Date now = new Date();
        Date validity = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
    }

    public String getUsernameOrEmail(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication validateUser(LoginRequest user) {
        String usernameOrEmail = user.getLoginId();
        Optional<Owner> optionalUser = ownerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        Owner item = optionalUser.get();
        if (user.getPassword().equals(item.getPassword())) {
            return new UsernamePasswordAuthenticationToken(item, null, Collections.emptyList());
        } else {
            throw new RuntimeException(messageSourceUtil.getMessage("invalid.password"));
        }

    }

}

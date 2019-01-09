package test.com.example.test.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import test.com.example.test.type.TokenType;
import test.com.example.test.model.User;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author quannguyen
 */
@Component
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;

    private String secret;

    private Long expiration;

    private String refreshSecret;

    private Long refreshExpiration;

    public JWTUtil(@Value("${jwt.token.secret}") final String secret,
				   @Value("${jwt.token.expiration}") final Long expiration,
				   @Value("${jwt.refresh.secret}") final String refreshSecret,
				   @Value("${jwt.refresh.expiration}") final Long refreshExpiration ){

    	this.secret = secret;
    	this.expiration = expiration;
    	this.refreshSecret = refreshSecret;
    	this.refreshExpiration = refreshExpiration;
	}

	public String generateToken(UserDetails userDetails, User user, TokenType tokenType) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		claims.put("enable", userDetails.isEnabled());
		claims.put("username", user.getUsername());
		return doGenerateToken(claims, user.getUsername(),tokenType);
	}

	private String doGenerateToken(Map<String, Object> claims, String username ,TokenType tokenType) {
		Long expirationTimeLong = tokenType == TokenType.ACCESS_TOKEN ? expiration : refreshExpiration; //in second
		
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, tokenType == TokenType.ACCESS_TOKEN ? secret.getBytes() : refreshSecret.getBytes())
				.compact();
	}
	
	public Boolean validateToken(String token,TokenType tokenType) {
		return !isTokenExpired(token,tokenType);
	}

	private Boolean isTokenExpired(String token,TokenType tokenType) {
		final Date expiration = getExpirationDateFromToken(token,tokenType);
		return expiration.before(new Date());
	}

	public Date getExpirationDateFromToken(String token,TokenType tokenType) {
		return getAllClaimsFromToken(token,tokenType).getExpiration();
	}

	public String getUsernameFromToken(String token,TokenType tokenType) {
		return getAllClaimsFromToken(token,tokenType).getSubject();
	}


	public Claims getAllClaimsFromToken(String token,TokenType tokenType) {
		return Jwts.parser().setSigningKey(tokenType == TokenType.ACCESS_TOKEN ? secret.getBytes() : refreshSecret.getBytes()).parseClaimsJws(token).getBody();
	}

}

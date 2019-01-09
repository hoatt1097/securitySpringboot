package test.com.example.test.configuration;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.com.example.test.type.TokenType;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		
		String username;
		try {
			username = jwtUtil.getUsernameFromToken(authToken,TokenType.ACCESS_TOKEN);
		} catch (Exception e) {
			username = null;
		}
		if (username != null) {
			Claims claims = jwtUtil.getAllClaimsFromToken(authToken,TokenType.ACCESS_TOKEN);
			if (jwtUtil.validateToken(authToken,TokenType.ACCESS_TOKEN)) {
				List<String> rolesMap = claims.get("role", List.class);

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					username,
					null,
						rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
				);
				return Mono.just(auth);
			} else {
				return Mono.empty();
			}
		} else {
			return Mono.empty();
		}
	}
}

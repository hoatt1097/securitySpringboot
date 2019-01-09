package test.com.example.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author quannguyen
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SecurityContextRepository securityContextRepository;

	public WebSecurityConfig(){

    }

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         final UnauthorizedAuthenticationEntryPoint entryPoint) {
		return http
				.exceptionHandling()
				.authenticationEntryPoint(entryPoint)
				.and()
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				.authorizeExchange()
				.pathMatchers(HttpMethod.OPTIONS).permitAll()
				.pathMatchers("/users/**").hasAuthority("ADMIN")
				.pathMatchers(HttpMethod.POST,"/token/**").permitAll()
				.anyExchange().authenticated()

				.and().build();
		
	}

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}

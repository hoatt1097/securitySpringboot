package test.com.example.test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {


    private String origin;

    @Autowired
    public UnauthorizedAuthenticationEntryPoint(@Value("${cors.access.origin}") final String origin){
        this.origin = origin;
    }

    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e) {
        return Mono.fromRunnable(() -> {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin",origin);
        });
    }
}
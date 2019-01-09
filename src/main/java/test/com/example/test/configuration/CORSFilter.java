package test.com.example.test.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 *
 * @author quannguyen
 */
@Configuration
@EnableWebFlux
public class CORSFilter implements WebFluxConfigurer {

	private  String origin;

	private  String method;

	private  String headers;

	public CORSFilter(@Value("${cors.access.origin}") final String origin,
					  @Value("${cors.access.method}") final String method,
					  @Value("${cors.access.header}") final String headers){
		this.origin = origin;
		this.method = method;
		this.headers = headers;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(origin)
				.allowedMethods(method.split(", "))
				.allowedHeaders(headers.split(", "))
				.maxAge(86400);
	}
}
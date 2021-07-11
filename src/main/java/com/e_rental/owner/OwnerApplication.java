package com.e_rental.owner;

import com.e_rental.owner.security.OAuth2.OAuth2Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OAuth2Properties.class)
public class OwnerApplication {
	public static void main(String[] args) {
		SpringApplication.run(OwnerApplication.class, args);
	}

}

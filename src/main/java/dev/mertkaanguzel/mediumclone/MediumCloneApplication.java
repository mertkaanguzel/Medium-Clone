package dev.mertkaanguzel.mediumclone;

import dev.mertkaanguzel.mediumclone.config.RsaKeyProperties;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
@SecurityScheme(name = "mediumapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, bearerFormat =  "JWT")
@EnableConfigurationProperties(RsaKeyProperties.class)
public class MediumCloneApplication {
	@Bean
	public Clock clock() {
		return Clock.systemUTC();
	}
	public static void main(String[] args) {
		SpringApplication.run(MediumCloneApplication.class, args);
	}

}

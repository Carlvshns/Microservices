package devdojo.academy.carl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import devdojo.academy.core.property.JwtConfiguration;

@SpringBootApplication
@EntityScan({"devdojo.academy.core.model"})
@EnableJpaRepositories({"devdojo.academy.core.repository"})
@EnableConfigurationProperties(value = JwtConfiguration.class)
@ComponentScan("devdojo.academy")
public class CarlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarlApplication.class, args);
	}

}

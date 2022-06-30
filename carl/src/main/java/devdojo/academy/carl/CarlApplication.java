package devdojo.academy.carl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"devdojo.academy.core.model"})
@EnableJpaRepositories({"devdojo.academy.core.repository"})
public class CarlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarlApplication.class, args);
	}

}

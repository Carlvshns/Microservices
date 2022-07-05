package devdojo.academy.carl.docs;

import org.springframework.context.annotation.Configuration;

import devdojo.academy.core.docs.BaseSwaggerConfig;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig{

    public SwaggerConfig() {
        super("devdojo.academy.carl.controller");
    }
}

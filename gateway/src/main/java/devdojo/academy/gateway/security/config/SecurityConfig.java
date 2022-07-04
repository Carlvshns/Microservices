package devdojo.academy.gateway.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import devdojo.academy.core.property.JwtConfiguration;
import devdojo.academy.gateway.security.filter.GatewayJwtTokenAuthorizationFilter;
import devdojo.academy.security.config.SecurityTokenConfig;
import devdojo.academy.security.token.converter.TokenConverter;

@EnableWebSecurity
public class SecurityConfig extends SecurityTokenConfig{

    private final TokenConverter tokenConverter;

    public SecurityConfig(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        super(jwtConfiguration);
        this.tokenConverter = tokenConverter;
    } 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter
        (new GatewayJwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter),
        UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}

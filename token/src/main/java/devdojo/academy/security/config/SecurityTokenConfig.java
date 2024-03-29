package devdojo.academy.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import devdojo.academy.core.property.JwtConfiguration;

public class SecurityTokenConfig extends WebSecurityConfigurerAdapter{

    protected final JwtConfiguration jwtConfiguration;

    public SecurityTokenConfig(JwtConfiguration jwtConfiguration){
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().configurationSource
        (request -> new CorsConfiguration().applyPermitDefaultValues())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().exceptionHandling().authenticationEntryPoint
        ((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers(jwtConfiguration.getLoginUrl(), "/**/swagger-ui.html").permitAll()
        .antMatchers
(HttpMethod.GET,
 "/**/swagger-resources/**", "/**/webjars/springfox-swagger-ui", "**/v2/api-docs/**").permitAll()
        .antMatchers("/course/v1/admin/**")
        .hasRole("ADMIN")
        .antMatchers("/auth/user/**")
        .hasAnyRole("ADMIN", "USER")
        .anyRequest().authenticated();
    }
}


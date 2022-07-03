package devdojo.academy.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import devdojo.academy.core.property.JwtConfiguration;

public class SecurityTokenConfig extends WebSecurityConfigurerAdapter{

    protected final JwtConfiguration jwtConfiguation;

    public SecurityTokenConfig(JwtConfiguration jwtConfiguation){
        this.jwtConfiguation = jwtConfiguation;
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
        .antMatchers(jwtConfiguation.getLoginUrl()).permitAll()
        .antMatchers("/course/admin/**")
        .hasRole("ADMIN").anyRequest().authenticated();
    }
}


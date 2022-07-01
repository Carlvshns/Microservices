package devdojo.academy.auth.security.config;

import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import devdojo.academy.core.property.JwtConfiguration;

@EnableWebSecurity
public class SecutiryCredentialsConfig extends WebSecurityConfigurerAdapter{
    
    private final UserDetailsService userDetailsService;

    private final JwtConfiguration jwtConfiguation;


    public SecutiryCredentialsConfig(UserDetailsService userDetailsService, JwtConfiguration jwtConfiguation){
        this.userDetailsService = userDetailsService;
        this.jwtConfiguation = jwtConfiguation;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().configurationSource
        (request -> new CorsConfiguration().applyPermitDefaultValues())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().exceptionHandling().authenticationEntryPoint
        ((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and().addFilter(new UsernamePasswordAuthenticationFilter()).authorizeRequests()
        .antMatchers(jwtConfiguation.getLoginUrl()).permitAll()
        .antMatchers("/course/admin/**")
        .hasRole("ADMIN").anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

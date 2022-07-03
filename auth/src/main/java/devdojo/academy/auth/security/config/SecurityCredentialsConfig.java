package devdojo.academy.auth.security.config;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import devdojo.academy.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import devdojo.academy.core.property.JwtConfiguration;
import devdojo.academy.security.config.SecurityTokenConfig;
import devdojo.academy.security.token.creator.TokenCreator;

@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig{
    
    private final UserDetailsService userDetailsService;
    private final TokenCreator tokenCreator;

    public SecurityCredentialsConfig(JwtConfiguration jwtConfiguation,
    @Qualifier("userDeatilsServiceImpl") UserDetailsService userDetailsService
    , TokenCreator tokenCreator){
        super(jwtConfiguation);
        this.userDetailsService = userDetailsService;
        this.tokenCreator = tokenCreator;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter
        (authenticationManager(), jwtConfiguation, tokenCreator));
        super.configure(http);
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

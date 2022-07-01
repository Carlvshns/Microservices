package devdojo.academy.auth.security.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import devdojo.academy.core.model.ApplicationUser;
import devdojo.academy.core.property.JwtConfiguration;
import lombok.SneakyThrows;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
            JwtConfiguration jwtConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
    }
    
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Attempting authentication. . ."); //Or log.info
        
            ApplicationUser applicationUser = new ApplicationUser();
            try {
                applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        if(applicationUser == null){
            throw new UsernameNotFoundException("Unable to retrieve the username or password");
        }

        System.out.println("Creating the authentication object for the user {"+applicationUser.getUsername()+"} and calling UserDetailsServiceImpl loadUserByUsername"); //Or log.info
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken
        (applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());

        usernamePasswordAuthenticationToken.setDetails(applicationUser);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        System.out.println("Authentication was sucessful for the user {"+auth.getName()+"}, genereting JWE Token"); //Or log.info
    }

    private SignedJWT createSignedJWT(Authentication auth){
        System.out.println("Starting to create the signed JWT");
        ApplicationUser applicationuser = (ApplicationUser) auth.getPrincipal();

    }

    private JWTClaimsSet createJWtClaimsSet(Authentication auth, ApplicationUser applicationUser){
        System.out.println("Creating the JWtClaimsSet Object for "+applicationUser); //Or log.info
        return new JWTClaimsSet.Builder()
        .subject(applicationUser.getUsername())
        .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .issuer("http://academy.devdojo")
        .issueTime(new Date())
        .expirationTime(new date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
        .build();
    }
}

package devdojo.academy.auth.security.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import devdojo.academy.core.model.ApplicationUser;
import devdojo.academy.core.property.JwtConfiguration;
import devdojo.academy.security.token.creator.TokenCreator;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;
    private final TokenCreator tokenCreator;
    
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
            JwtConfiguration jwtConfiguration, TokenCreator tokenCreator) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.tokenCreator = tokenCreator;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
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

        SignedJWT signedJWT = null;
        try {
            signedJWT = tokenCreator.createSignedJWT(auth);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        String encryptedToken = "";
        try {
            encryptedToken = tokenCreator.encryptToken(signedJWT);
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        
        System.out.println("Token generated succesfully, adding it to the response header"); //Or log.info

        response.addHeader("Acess-Control-Expose-Headers", 
        "XSRF-TOKEN, "+jwtConfiguration.getHeader().getName());
    
        response.addHeader(jwtConfiguration.getHeader().getName(), 
        jwtConfiguration.getHeader().getPrefix() + encryptedToken);
    }

}

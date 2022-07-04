package devdojo.academy.security.util;

import java.util.List;
import java.util.stream.Collectors;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import devdojo.academy.core.model.ApplicationUser;

public class SecurityContextUtil {
    
    private SecurityContextUtil(){

    }

    public static void setSecurityContext(SignedJWT signedJWT){
        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            String username = jwtClaimsSet.getSubject();
            if(username == null){
                throw new JOSEException("Username missing from JWT");
            }
            List<String> authorities = jwtClaimsSet.getStringListClaim("authorities");
            
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setId(jwtClaimsSet.getLongClaim("userId"));
            applicationUser.setUsername(username);
            applicationUser.setRole(String.join(",", authorities));
            
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken
            (applicationUser, null, createAuthorities(authorities));

            auth.setDetails(signedJWT.serialize());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            System.out.println("Error setting security context");
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities){
        
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

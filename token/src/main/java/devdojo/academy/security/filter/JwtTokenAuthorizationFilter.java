package devdojo.academy.security.filter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import devdojo.academy.core.property.JwtConfiguration;
import devdojo.academy.security.token.converter.TokenConverter;
import devdojo.academy.security.util.SecurityContextUtil;

public class JwtTokenAuthorizationFilter extends OncePerRequestFilter{

    protected final JwtConfiguration jwtConfiguration;
    protected final TokenConverter tokenConverter;

    public JwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        this.jwtConfiguration = jwtConfiguration;
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(jwtConfiguration.getHeader().getName());

        if(header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())){
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

        try {

            SecurityContextUtil.setSecurityContext
            (StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) 
            ? validate(token) : decryptValidating(token));

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }

    private SignedJWT decryptValidating(String encryptedToken) throws ParseException, JOSEException, AccessDeniedException {
        String signedToken;
        
        signedToken = tokenConverter.decryptToken(encryptedToken);

        tokenConverter.validateTokenSignature(signedToken);

        return SignedJWT.parse(signedToken);
    }

    private SignedJWT validate(String signedToken) throws ParseException, AccessDeniedException, JOSEException {
        
        tokenConverter.validateTokenSignature(signedToken);
        
        return SignedJWT.parse(signedToken);
    }
    
}

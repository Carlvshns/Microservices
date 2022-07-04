package devdojo.academy.gateway.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

import devdojo.academy.core.property.JwtConfiguration;
import devdojo.academy.security.filter.JwtTokenAuthorizationFilter;
import devdojo.academy.security.token.converter.TokenConverter;
import devdojo.academy.security.util.SecurityContextUtil;

public class GatewayJwtTokenAuthorizationFilter extends JwtTokenAuthorizationFilter{
    
    public GatewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        super(jwtConfiguration, tokenConverter);
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

        String signedToken = "";
        try {

            signedToken = tokenConverter.decryptToken(token);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        try {

            tokenConverter.validateTokenSignature(signedToken);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        try {
            SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(jwtConfiguration.getType().equalsIgnoreCase("signed")){
            RequestContext.getCurrentContext().addZuulRequestHeader("Authorization",jwtConfiguration.getHeader().getPrefix() + signedToken);
        }

        chain.doFilter(request, response);
    }
}

package devdojo.academy.security.token.converter;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.stereotype.Service;

import devdojo.academy.core.property.JwtConfiguration;

@Service
public class TokenConverter {

    private final JwtConfiguration jwtConfiguration;

    public TokenConverter(JwtConfiguration jwtConfiguration){
        this.jwtConfiguration = jwtConfiguration;
    }

    public String decryptToken(String encryptedToken) throws ParseException, JOSEException{
        System.out.println("Decrypting Token...");

        JWEObject jweObject =  JWEObject.parse(encryptedToken);

        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());

        jweObject.decrypt(directDecrypter);

        System.out.println("Token decrypted, returning signed token. . .");
        
        return jweObject.getPayload().toSignedJWT().serialize();
    }

    public void validateTokenSignature(String signedToken) throws ParseException, JOSEException, AccessDeniedException{
        System.out.println("Starting method to validate token signature...");
        
        SignedJWT signedJWT = SignedJWT.parse(signedToken);

        System.out.println("Token Parsed! Retrieving public key from signed token");

        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());

        System.out.println("Public key revtrieved, validating signature...");

        if(!signedJWT.verify(new RSASSAVerifier(publicKey))){
            throw new AccessDeniedException("Invalid token signature!");
        }

        System.out.println("The token has a valid signature");
    }
}
package devdojo.academy.security.token.creator;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import devdojo.academy.core.model.ApplicationUser;
import devdojo.academy.core.property.JwtConfiguration;

@Service
public class TokenCreator {

    private final JwtConfiguration jwtConfiguration;
    
    public TokenCreator(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    public SignedJWT createSignedJWT(Authentication auth) throws NoSuchAlgorithmException, JOSEException {
        System.out.println("Starting to create the signed JWT"); //Or log.info
        
        ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();
        
        JWTClaimsSet jwtClaimSet = createJWTClaimsSet(auth, applicationUser);

        KeyPair rsaKeys = generateKeyPair();
        
        System.out.println("Building JWK from the RSA Keys"); //Or log.info

        JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic())
        .keyID(UUID.randomUUID().toString()).build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
        .jwk(jwk).type(JOSEObjectType.JWT).build(), jwtClaimSet);

        System.out.println("Signing the token with private RSA Key");

        RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

        signedJWT.sign(signer);

        System.out.println("Serialized token {"+signedJWT.serialize()+"}");

        return signedJWT;
    }

    private JWTClaimsSet createJWTClaimsSet(Authentication auth, ApplicationUser applicationUser){
        System.out.println("Creating the JWtClaimsSet Object for "+applicationUser); //Or log.info
        
        return new JWTClaimsSet.Builder()
        .subject(applicationUser.getUsername())
        .claim("authorities", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .issuer("http://academy.devdojo")
        .issueTime(new Date())
        .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration() * 1000)))
        .build();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException{
        System.out.println("Generating RSA 2048 bits Keys"); //Or log.info

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();
    }

    public String encryptToken(SignedJWT signedJWT) throws JOSEException{
        System.out.println("Starting encrypting"); //Or log.info

        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
        
        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
        .contentType("JWT").build(), new Payload(signedJWT));

        System.out.println("Encryping token with system private key");

        jweObject.encrypt(directEncrypter);

        System.out.println("Token encrypted");

        return jweObject.serialize();
    }
}

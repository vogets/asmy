package com.bdt.asmy.utilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bdt.asmy.user.UserData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JWTProvider {

    @Value("${jwt.issuer}")
    private String JWT_ISSUER;
    @Value("${jwt.tokenvalidtime}")
    private Long JWT_TOKEN_LIMIT;
    @Value("${jwt.WithAudience}")
    private String JWT_AUIDIENCE;
    @Value("${jwt.AUTHORITIES}")
    private String JWT_AUTHORITIES;
    @Value("${jwt.token}")
    private String JWT_Token;

    public String JwtTokenGenerate(UserData userData)
    {

        String[] permit=getPermissionFromUser(userData);
        String JWTToken= JWT.create()
                .withIssuer(JWT_ISSUER)
                .withAudience(JWT_AUIDIENCE)
                .withIssuedAt(new Date())
                .withSubject(userData.getUsername())
                .withArrayClaim(JWT_AUTHORITIES,permit)
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * JWT_TOKEN_LIMIT))
                .sign(HMAC512(JWT_Token.getBytes()));
        return JWTToken;
        
    }

    private String[] getPermissionFromUser(UserData userData) {
        List<String> permissions=new ArrayList<>();
        for(GrantedAuthority grantedAuthority:userData.getAuthorities())
        {
            permissions.add(grantedAuthority.getAuthority());
        }
        return permissions.toArray(new String[0]);
    }

    public List<GrantedAuthority> getAuthorities(String token)
    {
        String[] permit=getPermissionsFromToken(token);
        return Stream.of(permit).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    public Authentication getAuthentication(String userName,
                                            List<GrantedAuthority> authorities,
                                            HttpServletRequest request)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new
                UsernamePasswordAuthenticationToken(userName,null,authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthenticationToken;
    }
    public boolean isTokenValid(String userName,String token)
    {
        JWTVerifier verifier=getJWTConfirmation();
        return StringUtils.isNotEmpty(userName) &&!isTokenExpired(verifier,token);
    }
    public String getSubject(String token)
    {
        JWTVerifier verifier=getJWTConfirmation();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token)
    {
        Date expiration=verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private JWTVerifier getJWTConfirmation()
    {
        JWTVerifier confirm = null;
        try
        {
          Algorithm algorithm=HMAC512(JWT_Token);
          confirm=JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
        }
        catch (JWTVerificationException exception)
        {
            throw new JWTVerificationException("Token Cannot be Verified");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return confirm;
    }

    private String[] getPermissionsFromToken(String token)
    {
JWTVerifier confirm=getJWTConfirmation();
return confirm.verify(token).getClaim(JWT_AUTHORITIES).asArray(String.class);
    }

}

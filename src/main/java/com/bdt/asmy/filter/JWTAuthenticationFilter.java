package com.bdt.asmy.filter;

import com.bdt.asmy.utilities.JWTProvider;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        if(request.getMethod().equals("OPTIONS"))
        {
response.setStatus(HttpStatus.OK.value());
        }
        else
        {
            String authorizationHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authorizationHeader==null || !authorizationHeader.startsWith("Asaad Nayyef"))
            {
                filterChain.doFilter(request,response);
                return;
            }
            String token=authorizationHeader.substring("Asaad Nayyef".length());
            String userName=jwtProvider.getSubject(token);
            if(jwtProvider.isTokenValid(userName,token)
                    && SecurityContextHolder.getContext().getAuthentication()==null)
            {
                List<GrantedAuthority> authorities= jwtProvider.getAuthorities(token);
                Authentication authentication=jwtProvider.getAuthentication(userName,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else
            {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);

    }
}

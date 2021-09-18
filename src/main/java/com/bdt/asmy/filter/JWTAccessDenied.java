package com.bdt.asmy.filter;

import com.bdt.asmy.response.MicroServiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JWTAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        MicroServiceResponse microServiceResponse=new MicroServiceResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase(),"You do not have permissions to access this resource");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream outputStream=response.getOutputStream();
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.writeValue(outputStream,microServiceResponse);
        outputStream.flush();

    }
}

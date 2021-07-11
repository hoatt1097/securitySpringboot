package com.e_rental.owner.security;

import com.e_rental.owner.enums.StatusCode;
import com.e_rental.owner.dto.responses.Response;
import com.e_rental.owner.utils.MessageSourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Response res = new Response();
        res.setMessage(messageSourceUtil.getMessage("access.deny"));
        res.setCode(StatusCode.ACCESS_DENY.getCode());
        PrintWriter out = response.getWriter();
        String body = ow.writeValueAsString(res);
        out.print(body);
        out.flush();
//        MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Unauthorized path"));
    }
}

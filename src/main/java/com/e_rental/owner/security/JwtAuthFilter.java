package com.e_rental.owner.security;

import com.e_rental.owner.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(UserAuthenticationProvider userAuthenticationProvider, CustomUserDetailsService customUserDetailsService) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = userAuthenticationProvider.resolveToken(httpServletRequest);

        if (token != null && userAuthenticationProvider.validateToken(token)) {
            try {
                String userNameOrEmail = userAuthenticationProvider.getUsernameOrEmail(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userNameOrEmail);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                throw e;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


}

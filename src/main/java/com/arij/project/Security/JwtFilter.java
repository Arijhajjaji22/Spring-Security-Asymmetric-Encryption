package com.arij.project.Security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService ;
 

    @Override
    protected void doFilterInternal(
        @NonNull
        final HttpServletRequest request,
        @NonNull
        final HttpServletResponse response,
        @NonNull
        final FilterChain filterChain) throws ServletException, IOException {
       if (request.getServletPath().contains("/api/v1/auth")){
        filterChain.doFilter(request, response);
        return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt ;
        final String username ;

        if (authHeader == null || !authHeader.startsWith("Bearer") ){
            filterChain.doFilter((request), response);
            return ;
        }
        jwt = authHeader.substring(7);
        username = this.jwtService.extractUsername(jwt);

        if(username != null && SecurityContextHolder.getContext().getAuthentication()== null){
          final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

          if (this.jwtService.isTokenValid(jwt,userDetails.getUsername())){
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
             userDetails,
          null,
             userDetails.getAuthorities()
          );
          authToken.setDetails((new WebAuthenticationDetailsSource().buildDetails(request)));

          SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        }

       filterChain.doFilter(request, response);

    }
}

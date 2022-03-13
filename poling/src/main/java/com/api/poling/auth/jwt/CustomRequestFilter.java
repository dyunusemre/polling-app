package com.api.poling.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class CustomRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header)) {
            filterChain.doFilter(request, response);
            return;
        }
        setAuthentication(header);
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String header) {
        String username = jwtTokenService.getUsernameFromToken(header);
        List<Map<String, String>> roles = jwtTokenService.getRoleFromToken(header);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, roles.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

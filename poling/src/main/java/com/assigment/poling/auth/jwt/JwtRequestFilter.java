package com.assigment.poling.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

public class JwtRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String header = request.getHeader("Authorization");
			if (!StringUtils.hasText(header)) {
				filterChain.doFilter(request, response);
				return;
			}
			String username = TokenUtil.getUsernameFromToken(header);
			List<Map<String, String>> roles = TokenUtil.getRoleFromToken(header);
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
																					roles
																					.stream()
																					.map(m -> new SimpleGrantedAuthority(m.get("authority")))
																					.collect(Collectors.toSet()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException e) {
			throw new IllegalStateException("TOKEN_NOT_TRUSTED");
		}
		filterChain.doFilter(request, response);
	}

}

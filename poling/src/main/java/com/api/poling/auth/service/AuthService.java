package com.api.poling.auth.service;

import com.api.poling.config.CustomUserDetails;
import com.api.poling.auth.dto.LoginRequest;
import com.api.poling.auth.dto.LoginResponse;
import com.api.poling.auth.dto.SignupRequest;
import com.api.poling.auth.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtTokenService jwtTokenService;

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        userService.findUserByUsername(request.getUsername()).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
        return ResponseEntity.ok(buildLoginResponse(authenticate(request.getUsername(), request.getPassword())));
    }

    public ResponseEntity<LoginResponse> signup(SignupRequest request) {
        userService.signup(request);
        LoginResponse response = buildLoginResponse(authenticate(request.getUsername(), request.getPassword()));
        return ResponseEntity.ok(response);
    }

    private Authentication authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;
        } catch (Exception ex) {
            throw new RuntimeException("Bad credentials");
        }
    }

    private LoginResponse buildLoginResponse(Authentication authentication) {
        return LoginResponse.builder()
                .id(((CustomUserDetails) authentication.getPrincipal()).getUser().getId())
                .isAdmin(false)
                .token(jwtTokenService.generateToken(authentication))
                .build();
    }

}

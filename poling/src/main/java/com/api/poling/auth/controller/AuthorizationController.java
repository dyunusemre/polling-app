package com.api.poling.auth.controller;

import com.api.poling.auth.dto.SignupRequest;
import com.api.poling.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import com.api.poling.auth.dto.LoginRequest;
import com.api.poling.auth.dto.LoginResponse;

import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping(value = "/sign-in")
    @ApiOperation("Operates the authentication logic")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<LoginResponse> signUp(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

}

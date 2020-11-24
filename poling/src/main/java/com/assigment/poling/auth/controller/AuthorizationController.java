package com.assigment.poling.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.assigment.poling.auth.jwt.TokenUtil;
import com.assigment.poling.auth.model.CustomUserDetails;
import com.assigment.poling.auth.model.User;
import com.assigment.poling.auth.repository.UserRepository;
import com.assigment.poling.auth.request.RequestLogin;
import com.assigment.poling.auth.response.ResponseAuth;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthorizationController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	AuthenticationManager authManager;

	@PostMapping(value = "/sign-in")
	@ApiOperation("Operates the authentication logic")
	public ResponseEntity<ResponseAuth> login(@RequestBody RequestLogin request) {
		if(!userRepo.existsByUsername(request.getUsername())) {
			return new ResponseEntity<ResponseAuth>(new ResponseAuth(), HttpStatus.NOT_FOUND);
		}
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ResponseAuth response = new ResponseAuth();
		response.setId(((CustomUserDetails) authentication.getPrincipal()).getUser().getId());
		response.setAdmin(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
		response.setToken(TokenUtil.generateToken(authentication));
		return new ResponseEntity<ResponseAuth>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/sign-up")
	public ResponseEntity<ResponseAuth> signUp(@RequestBody RequestLogin request) {
		if(userRepo.existsByUsername(request.getUsername())) {
			return new ResponseEntity<ResponseAuth>(new ResponseAuth(), HttpStatus.CONFLICT);
		}
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole("ROLE_USER");
		userRepo.save(user);
		//Auth logic
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ResponseAuth response = new ResponseAuth();
		response.setId(((CustomUserDetails) authentication.getPrincipal()).getUser().getId());
		response.setAdmin(false);
		response.setToken(TokenUtil.generateToken(authentication));
		return new ResponseEntity<ResponseAuth>(response, HttpStatus.OK);
	}

}

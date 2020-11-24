package com.assigment.poling.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assigment.poling.auth.jwt.JwtRequestFilter;
import com.assigment.poling.auth.service.CustomUserDetailsService;

import static com.assigment.poling.auth.Roles.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final CustomUserDetailsService userService;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailsService userService) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui/", "/swagger-ui/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			csrf()
				.disable()
			.cors()
				.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
		.addFilterAfter(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers("/*").permitAll()
		.antMatchers("/api/question/poll-questions").hasAnyRole(USER.name(), ADMIN.name())
		.antMatchers("/api/question/send-answer").hasAnyRole(USER.name(), ADMIN.name())
		.antMatchers("/api/question/retrive-answer").hasAnyRole(USER.name(), ADMIN.name())
		.antMatchers("/api/question/create-question").hasAnyRole(USER.name(), ADMIN.name())
		.antMatchers("/api/question/**").hasAnyRole(ADMIN.name())
		.anyRequest()
		.authenticated();
	}
}

package com.api.poling.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.poling.auth.jwt.CustomRequestFilter;
import com.api.poling.auth.service.CustomUserDetailsService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomRequestFilter customRequestFilter;

    private static final String[] SWAGGER = {
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/",
            "/swagger-ui/**"
    };

    private static final String[] USER_END_POINT = {
            "/api/v1/question/poll-questions",
            "/api/v1/question/send-answer",
            "/api/v1/question/retrieve-answer",
            "/api/v1/question/create-question"
    };

    private static final String[] ADMIN_END_POINT = {
            "/api/question/**"
    };

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(SWAGGER).permitAll();
        http.authorizeRequests().antMatchers(USER_END_POINT).hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers(ADMIN_END_POINT).hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(customRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

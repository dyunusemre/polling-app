package com.api.poling.auth.service;

import com.api.poling.auth.dao.User;
import com.api.poling.auth.dto.SignupRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    void signup(SignupRequest request);

    Optional<User> findByUsername(String username);
}

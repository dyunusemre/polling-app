package com.api.poling.auth.service;

import com.api.poling.auth.dao.User;
import com.api.poling.auth.dto.SignupRequest;
import com.api.poling.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequest request) {
        validateUserByUsername(request.getUsername());
        userRepository.save(getUserBuilder(request));
    }

    private void validateUserByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username exists");
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    private User getUserBuilder(SignupRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER").
                build();
    }
}

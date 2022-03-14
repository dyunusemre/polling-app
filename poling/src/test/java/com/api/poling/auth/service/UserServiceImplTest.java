package com.api.poling.auth.service;

import com.api.poling.auth.dao.User;
import com.api.poling.auth.dto.SignupRequest;
import com.api.poling.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl underTestUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

    }

    @Test
    void canSignup() {
        SignupRequest request = SignupRequest.builder()
                .name("test")
                .username("testusername")
                .password("testpassword")
                .build();
        underTestUserService.signup(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User captured = userArgumentCaptor.getValue();
        assertThat(captured.getUsername()).isEqualTo(request.getUsername());
        assertThat(captured.getPassword()).isEqualTo(passwordEncoder.encode(request.getPassword()));
    }

    @Test
    void shouldThrownExceptionWhenExistingUsernameSignUp() {
        SignupRequest request = SignupRequest.builder()
                .name("test")
                .username("testusername")
                .password("testpassword")
                .build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(buildUser(request)));
        //given(userRepository.findByUsername(request.getUsername())).willReturn(Optional.of(buildUser(request)));
        assertThatThrownBy(() -> underTestUserService.signup(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username exists");

        verify(userRepository , never()).save(any());
    }

    @Test
    void findUserByUsername() {
        when(userRepository.findByUsername("testUserName")).thenReturn(Optional.of(User.builder()
                .username("testUserName")
                .build()));

        Optional<User> user = underTestUserService.findUserByUsername("testUserName");
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo("testUserName");
    }

    @Test
    void canFindUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(User.builder()
                .id("1")
                .build()));
        Optional<User> user = underTestUserService.findUserById("1");
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getId()).isEqualTo("1");
    }

    private User buildUser(SignupRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role("ROLE_USER")
                .build();
    }
}
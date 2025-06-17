package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.AuthenticationResponse;
import com.waildevil.job_board_api.dto.RegisterRequest;
import com.waildevil.job_board_api.entity.Role;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.repository.UserRepository;
import com.waildevil.job_board_api.security.JwtService;
import com.waildevil.job_board_api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegister_shouldReturnJwtToken() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("test@example.com");
        req.setPassword("password123");
        req.setName("Wail");
        req.setRole(Role.CANDIDATE); // ✅ not String.valueOf(...)

        User savedUser = new User();
        savedUser.setEmail(req.getEmail());
        savedUser.setPassword("encoded-password");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(anyString())).thenReturn("mocked-jwt-token");

        AuthenticationResponse response = authService.register(req);

        assertEquals("mocked-jwt-token", response.getToken());
    }


}


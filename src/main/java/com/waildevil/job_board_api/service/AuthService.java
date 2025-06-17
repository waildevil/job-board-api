package com.waildevil.job_board_api.service;


import com.waildevil.job_board_api.dto.AuthenticationResponse;
import com.waildevil.job_board_api.dto.LoginRequest;
import com.waildevil.job_board_api.dto.RegisterRequest;
import com.waildevil.job_board_api.entity.Role;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.exception.ApiException;
import com.waildevil.job_board_api.repository.UserRepository;
import com.waildevil.job_board_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Role requestedRole = request.getRole();

        if (requestedRole == Role.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot register as an admin.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(requestedRole)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}

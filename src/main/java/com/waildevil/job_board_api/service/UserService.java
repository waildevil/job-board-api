package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.UserDto;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.exception.ApiException;
import com.waildevil.job_board_api.exception.InvalidOldPasswordException;
import com.waildevil.job_board_api.mapper.UserMapper;
import com.waildevil.job_board_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(UserDto dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow();
        userMapper.updateEntity(user, dto);
        return userRepository.save(user);
    }

    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Current passowrd is incorret");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }




    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

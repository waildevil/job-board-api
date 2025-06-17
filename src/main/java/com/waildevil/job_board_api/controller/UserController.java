package com.waildevil.job_board_api.controller;

import com.waildevil.job_board_api.dto.PasswordChangeRequest;
import com.waildevil.job_board_api.dto.UserDto;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.mapper.UserMapper;
import com.waildevil.job_board_api.repository.UserRepository;
import com.waildevil.job_board_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Users", description = "User account management and profile endpoints")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user", description = "Returns the profile details of the authenticated user")
    public ResponseEntity<UserDto> getCurrentUser(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Returns a list of all registered users (Admin only)")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        var users = userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Returns the user details for a given ID (Admin only)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new user", description = "Creates a new user (Admin only)")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto dto) {
        var createdUser = userService.createUser(dto);
        return ResponseEntity.ok(userMapper.toDto(createdUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user by ID", description = "Updates user information for the specified ID (Admin only)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto dto) {
        var updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update current user", description = "Updates the profile of the authenticated user")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody @Valid UserDto dto, Authentication auth) {
        String email = auth.getName();
        var user = userRepository.findByEmail(email).orElseThrow();
        dto.setRole(user.getRole());
        userMapper.updateEntity(user, dto);
        var updatedUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change password", description = "Allows the authenticated user to change their password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordChangeRequest req, Authentication auth) {
        userService.updatePassword(auth.getName(), req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a user by ID (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


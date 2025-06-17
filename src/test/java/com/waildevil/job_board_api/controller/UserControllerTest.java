package com.waildevil.job_board_api.controller;

import com.waildevil.job_board_api.dto.UserDto;
import com.waildevil.job_board_api.entity.Role;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.mapper.UserMapper;
import com.waildevil.job_board_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_shouldReturnUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("Wail")
                .email("test@example.com")
                .role(Role.CANDIDATE)
                .build();

        UserDto userDto = new UserDto(1L, "Wail", "test@example.com", "CANDIDATE", null);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Wail"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_shouldReturnNotFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }
}

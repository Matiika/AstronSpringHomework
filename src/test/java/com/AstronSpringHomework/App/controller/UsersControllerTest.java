package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.dto.userDto.UserDTO;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;
import com.AstronSpringHomework.App.model.User;
import com.AstronSpringHomework.App.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAge(25);
        testUser = userRepository.save(testUser);
    }

    // POST /api/users TESTS

    @Test
    void createUserSuccess() throws Exception {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName("New User");
        createDTO.setEmail("new@example.com");
        createDTO.setAge(30);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UserDTO user = objectMapper.readValue(json, UserDTO.class);

        assertEquals("New User", user.getName());
        assertEquals("new@example.com", user.getEmail());
        assertEquals(30, user.getAge());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void createUserDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName("Another User");
        createDTO.setEmail("test@example.com"); // уже существует
        createDTO.setAge(30);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserInvalidData_ShouldReturnBadRequest() throws Exception {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName(""); // пустое имя
        createDTO.setEmail("invalid-email"); // невалидный email
        createDTO.setAge(-5); // отрицательный возраст

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    // GET /api/users TESTS

    @Test
    void getAllUsers_Success() throws Exception {
        // Создаем дополнительного пользователя
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setAge(35);
        userRepository.save(user2);

        MvcResult result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<Map<String, Object>> users = objectMapper.readValue(json, new TypeReference<>() {});

        assertEquals(2, users.size());

        assertEquals(testUser.getName(), users.get(0).get("name"));
        assertEquals(testUser.getEmail(), users.get(0).get("email"));

        assertEquals("User 2", users.get(1).get("name"));
        assertEquals("user2@example.com", users.get(1).get("email"));
    }

    @Test
    void getAllUsers_EmptyDatabase() throws Exception {
        userRepository.deleteAll();

        String response = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> users = objectMapper.readValue(response, List.class);
        assertTrue(users.isEmpty());
    }

    // GET /api/users/{id} TESTS

    @Test
    void getUserById_Success() throws Exception {
        String json = mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO user = objectMapper.readValue(json, UserDTO.class);

        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getAge(), user.getAge());
    }

    @Test
    void getUserById_NotFound_ShouldReturn404() throws Exception {
        int nonExistentId = 999;

        mockMvc.perform(get("/api/users/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Пользователь с id=" + nonExistentId + " не найден")));
    }

    // ==================== PUT /api/users/{id} TESTS ====================

    @Test
    void updateUser_Success() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setAge(40);

        String jsonResponse = mockMvc.perform(put("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO updatedUser = objectMapper.readValue(jsonResponse, UserDTO.class);

        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(40, updatedUser.getAge());
    }

    @Test
    void updateUser_DuplicateEmail() throws Exception {
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setAge(35);
        user2 = userRepository.save(user2);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setEmail("user2@example.com"); // пытаемся test-пользователю установить email второго пользователя

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Пользователь с таким Email уже существует"));
    }

    @Test
    void updateUser_InvalidData() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();

        //данные с ошибками
        updateDTO.setName("");
        updateDTO.setEmail("invalid-email");
        updateDTO.setAge(-10);

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest());
    }

    // DELETE /api/users/{id} TESTS

    @Test
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/users/" + testUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        int nonExistentId = 999;

        mockMvc.perform(delete("/api/users/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Пользователь с id=" + nonExistentId + " не найден")));
    }



}
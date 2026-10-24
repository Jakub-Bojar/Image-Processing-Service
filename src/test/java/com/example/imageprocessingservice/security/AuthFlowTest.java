package com.example.imageprocessingservice.security;

import com.example.imageprocessingservice.repository.UserRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRecordRepository userRecordRepository;

    @Test
    void userCanRegisterLoginAndAccessProtectedEndpoint() throws Exception {
        String email = "auth-flow@example.com";
        userRecordRepository.deleteAll();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"auth-flow","email":"auth-flow@example.com","password":"password123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"auth-flow@example.com","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.token").isString())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(loginResponse)
                .path("response")
                .path("token")
                .asText();

        mockMvc.perform(get("/api/test")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"auth-flow@example.com","password":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}

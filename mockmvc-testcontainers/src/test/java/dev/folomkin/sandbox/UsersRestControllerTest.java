package dev.folomkin.sandbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Testcontainers
@SpringBootTest(classes = TestBeans.class)
@AutoConfigureMockMvc
class UsersRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment environment;

    @Test
    void findAllUsers_ReturnsUsersList() throws Exception {
        assertEquals("org.postgresql.Driver", environment.getProperty("postgresql.driver"));
        this.mockMvc.perform(get("/api/users"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "username": "j.jameson"},
                                {"id": 2, "username": "j.walker"},
                                {"id": 3, "username": "j.daniels"},
                                {"id": 4, "username": "j.dewar"}
                                ]""")
                );
    }
}
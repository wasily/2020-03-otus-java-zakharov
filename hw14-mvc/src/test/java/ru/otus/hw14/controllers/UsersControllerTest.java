package ru.otus.hw14.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DbServiceException;
import ru.otus.hw14.core.service.DbServiceUserImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    private static final String USER_API_URL = "/api/users";

    private MockMvc mvc;

    @Mock
    private DbServiceUserImpl dbServiceUser;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new UsersController(dbServiceUser)).build();
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Gson gson = new GsonBuilder().create();
        long id = 2;
        User expectedUser = new User(id, "test", "test", "test");
        given(dbServiceUser.getUser(id)).willReturn(Optional.of(expectedUser));
        mvc.perform(get(USER_API_URL + "/" + id).param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(expectedUser)));
    }

    @Test
    void shouldReturn404WhenNoUserFoundById() throws Exception {
        long id = 2_000_000;
        Optional<User> optionalUser = Optional.empty();
        given(dbServiceUser.getUser(id)).willReturn(optionalUser);
        mvc.perform(get(USER_API_URL + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        Gson gson = new GsonBuilder().create();
        List<User> allUsers = List.of(new User(1, "1", "1", "1"),
                new User(2, "2", "2", "2"));
        given(dbServiceUser.getAllUsers()).willReturn(allUsers);
        mvc.perform(get(USER_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(gson.toJson(allUsers)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        long id = 2;
        given(dbServiceUser.deleteUser(id)).willReturn(1);
        mvc.perform(delete(USER_API_URL + "/" + id))
                .andExpect(status().isOk());
        given(dbServiceUser.deleteUser(id)).willReturn(0);
        mvc.perform(delete(USER_API_URL + "/" + id))
                .andExpect(status().isNotFound());
        given(dbServiceUser.deleteUser(id)).willReturn(-1);
        mvc.perform(delete(USER_API_URL + "/" + id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldSaveUser() throws Exception {
        Gson gson = new GsonBuilder().create();
        User expectedUser = new User(99, "new name", "new login", "password");
        mvc.perform(post(USER_API_URL).contentType(APPLICATION_JSON).content(gson.toJson(expectedUser)))
                .andExpect(status().isCreated());
        verify(dbServiceUser).saveUser(expectedUser);
    }

    @Test
    void shouldNotSaveOnConflict() throws Exception {
        Gson gson = new GsonBuilder().create();
        User conflictUser = new User(99, "conflict", "conflict", "conflict");
        given(dbServiceUser.saveUser(conflictUser)).willThrow(DbServiceException.class);
        mvc.perform(post(USER_API_URL).contentType(APPLICATION_JSON).content(gson.toJson(conflictUser)))
                .andExpect(status().isConflict());
    }
}
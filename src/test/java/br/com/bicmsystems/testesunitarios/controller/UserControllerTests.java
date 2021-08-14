package br.com.bicmsystems.testesunitarios.controller;

import br.com.bicmsystems.testesunitarios.exceptions.NotFoundException;
import br.com.bicmsystems.testesunitarios.model.UserModel;
import br.com.bicmsystems.testesunitarios.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

    private final String name = "user", document = "00000000";
    private final Integer age = 30;
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void shouldSaveUser_WhenSaveUserIsCalled() throws Exception {

        UserModel user1 = new UserModel(100L, name, age, document);
        when(userService.save(any())).thenReturn(user1);
        String inputJson = this.mapToJson(user1);
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(result -> assertTrue(result.getResponse().getStatus() ==
                        HttpStatus.CREATED.value()));
        verify(userService, times(1)).save(any());
    }


    @Test
    public void shouldReturn500_WhenHappensSomethingWrongWhileSaveUserIsCalled() throws Exception {

        UserModel user1 = new UserModel(100L, name, age, document);
        String inputJson = this.mapToJson(user1);
        
        doThrow(new Exception("An error occured when save user data")).when(userService).save(any());
        
        MvcResult mvcResult = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
        				.andReturn();
        
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(userService, times(1)).save(any());
        
    }

    
    @Test
    public void shouldDeleteUser_WhenDeleteUserIsCalled() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(
                delete("/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.NO_CONTENT.value());
        verify(userService, times(1)).delete(any());

    }

    @Test
    public void shouldNotDeleteUser_WhenDeleteUserIsCalledForANotExistingUser() throws Exception {

        doThrow(new NotFoundException("User not found"))
                .when(userService).delete(any());
        MvcResult mvcResult = this.mockMvc.perform(
                delete("/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.NOT_FOUND.value());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("User not found"));
        verify(userService, times(1)).delete(any());

    }
    
    @Test
    public void shouldReturn500_WhenHappensSomethingWrongWhileDeleteUserIsCalled() throws Exception {

        doThrow(new Exception("An error occured when delete user data")).when(userService).delete(any());
        
        MvcResult mvcResult = this.mockMvc.perform(delete("/users/100")
                        .contentType(MediaType.APPLICATION_JSON))
        				.andReturn();
        
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(userService, times(1)).delete(any());
        
    }

    @Test
    public void shouldUpdateUser_WhenUpdateUserIsCalled() throws Exception {

        UserModel user1 = new UserModel(100L, name, age, document);
        String inputJson = this.mapToJson(user1);
        MvcResult mvcResult = this.mockMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson)
        ).andReturn();
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());
        verify(userService, times(1)).update(any());

    }

    @Test
    public void shouldNotUpdateUser_WhenUpdateUserIsCalledForANotExistingUser() throws Exception {

        UserModel user1 = new UserModel(100L, name, age, document);
        String inputJson = this.mapToJson(user1);
        doThrow(new NotFoundException("User not found"))
                .when(userService).update(any());
        MvcResult mvcResult = this.mockMvc.perform(
                put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson)
        ).andReturn();
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.NOT_FOUND.value());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("User not found"));
        verify(userService, times(1)).update(any());

    }

    @Test
    public void shouldReturn500_WhenHappensSomethingWrongWhileUpdateUserIsCalled() throws Exception {
    	
    	UserModel user1 = new UserModel(100L, name, age, document);
        String inputJson = this.mapToJson(user1);
        doThrow(new Exception("An error occured when update user data")).when(userService).update(any());
        
        MvcResult mvcResult = this.mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
        				.andReturn();
        
        assertTrue(mvcResult.getResponse().getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(userService, times(1)).update(any());
        
    }

    @Test
    public void shouldFindAllUsers_WhenFindAllUsersIsCalled() throws Exception {

        UserModel user1 = new UserModel(100L, name, age, document);
        UserModel user2 = new UserModel(150L, name + "1", age + 1, document + "-01");
        List<UserModel> userList = List.of(user1, user2);
        when(userService.findAllUsers()).thenReturn(userList);
        this.mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(document)))
                .andExpect(content().string(containsString(document + "-01")));
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    public void shouldFindUser_WhenFindUsersIsCalledForAExistingUser() throws Exception {

        UserModel user1 = new UserModel(120L, name, age, document);
        when(userService.findById(any())).thenReturn(user1);
        this.mockMvc.perform(get("/users/120")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(document)))
                .andExpect(content().string(containsString(name)));
        verify(userService, times(1)).findById(any());
    }

    @Test
    public void shouldNotFindUser_WhenFindUsersIsCalledForANotExistingUser() throws Exception {

        when(userService.findById(any())).thenThrow(new NotFoundException("User not found"));
        this.mockMvc.perform(get("/users/150")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result ->
                        assertEquals("User not found", result.getResolvedException().getMessage())
                );
        verify(userService, times(1)).findById(any());

    }

}

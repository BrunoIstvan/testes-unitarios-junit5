package br.com.bicmsystems.testesunitarios.service;

import br.com.bicmsystems.testesunitarios.exceptions.NotFoundException;
import br.com.bicmsystems.testesunitarios.model.UserModel;
import br.com.bicmsystems.testesunitarios.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @TestConfiguration
    static class UserServiceConfiguration {
        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private static final String name = "nome usuario";
    private static final Integer age = 40;
    private static final String document = "123.456.789-00";

    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void shouldInsertUserIntoDatabase_WhenSaveIsCalled() throws Exception {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        UserModel newUser = userService.save(user);
        assertTrue(newUser instanceof UserModel);
        verify(userRepository, times(1)).save(any());
        assertNotNull(newUser.getId());
        assertFirstUserFields();
        assertUserFields(newUser);
    }

    @Test
    public void shouldUpdateUserIntoDatabase_WhenSaveIsCalledForAExistingUser() throws Exception {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findAll()).thenReturn(List.of(user));
        UserModel newUser = userService.save(user);
        assertUserFields(newUser);
        newUser.setAge(55);
        newUser.setDocument("DOC.876.TYU-TT");
        userService.update(newUser);
        assertUserFields(newUser.getName(), newUser.getAge(), newUser.getDocument());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(2)).save(any());
    }

    @Test
    public void shouldThrowNotFoundException_WhenUpdateIsCalledForANotExistingUser() throws Exception {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        UserModel newUser = userService.save(user);
        assertUserFields(newUser);
        newUser.setAge(55);
        newUser.setDocument("DOC.876.TYU-TT");
        Assertions.assertThrows(NotFoundException.class, () -> {
            when(userRepository.findById(any())).thenReturn(Optional.empty());
            newUser.setId(150L);
            userService.update(newUser);
            verify(userRepository, times(1)).findById(any());
        });
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void shouldDeleteUserIntoDatabase_WhenDeleteIsCalledForAExistingUser() throws Exception {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        UserModel newUser = userService.save(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(newUser));
        doNothing().when(userRepository).delete(newUser);
        userService.delete(newUser.getId());
        verify(userRepository, times(1)).findById(newUser.getId());
        verify(userRepository, times(1)).delete(newUser);
    }

    @Test
    public void shouldThrowNotFoundException_WhenDeleteNotExistingUser() throws Exception {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        UserModel newUser = userService.save(user);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        
        Assertions.assertThrows(NotFoundException.class, () -> {
        	userService.delete(newUser.getId());
            verify(userRepository, times(1)).delete(newUser);
            when(userRepository.findById(any())).thenReturn(Optional.empty());
            UserModel foundUser = userService.findById(newUser.getId());
            assertNull(foundUser);
        });
    }


    @Test
    public void shouldFindUserFromDatabase_WhenFindByIdIsCalled() throws NotFoundException {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserModel foundUser = userService.findById(100L);
        assertNotNull(foundUser);
        assertUserFields(foundUser);

    }

    @Test
    public void shouldNotFoundUser_WhenFindByIdIsCalledForANotExistingUser() {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            UserModel foundUser = userService.findById(150L);
            assertNull(foundUser);
        });
    }

    private void assertFirstUserFields() {
        UserModel user = new UserModel(100L, name, age, document);
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserModel> users = userService.findAllUsers();
        assertEquals(1, users.size());
        assertEquals(name, users.get(0).getName());
        assertEquals(age, users.get(0).getAge());
        assertEquals(document, users.get(0).getDocument());
    }

    private void assertUserFields(UserModel user) {
        assertEquals(name, user.getName());
        assertEquals(age, user.getAge());
        assertEquals(document, user.getDocument());
    }

    private void assertUserFields(String pName, Integer pAge, String pDocumento) {
        List<UserModel> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals(pName, users.get(0).getName());
        assertEquals(pAge, users.get(0).getAge());
        assertEquals(pDocumento, users.get(0).getDocument());
    }

}

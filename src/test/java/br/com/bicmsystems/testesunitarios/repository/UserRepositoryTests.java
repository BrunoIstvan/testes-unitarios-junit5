package br.com.bicmsystems.testesunitarios.repository;

import br.com.bicmsystems.testesunitarios.model.UserModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private static final String name = "nome usuario";
    private static final Integer age = 40;
    private static final String document = "123.456.789-00";

    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void shouldInsertUserIntoDatabase_WhenSaveIsCalled() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        assertFirstUserFields();
        assertUserFields(newUser);
    }

    @Test
    public void shouldUpdateUserIntoDatabase_WhenSaveIsCalledForAExistingUser() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        assertUserFields(newUser);
        newUser.setAge(55);
        newUser.setDocument("DOC.876.TYU-TT");
        userRepository.save(newUser);
        assertUserFields(newUser.getId(), newUser.getName(), newUser.getAge(), newUser.getDocument());
    }

    @Test
    public void shouldDeleteUserIntoDatabase_WhenDeleteIsCalledForAExistingUser() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        assertUserFields(newUser);
        userRepository.delete(newUser);
        assertUserNotFound();
    }

    @Test
    public void shouldNotDeleteUserIntoDatabase_WhenDeleteIsCalledForANotExistingUser() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        assertUserFields(newUser);
        UserModel otherUser = new UserModel();
        userRepository.delete(otherUser);
        assertUserNotFound(10L);
    }

    @Test
    public void shouldFoundUser_WhenSearchingByExistingUserId() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        Optional<UserModel> foundUser = userRepository.findById(newUser.getId());
        assertTrue(foundUser.isPresent());
        assertUserFields(foundUser.get());
    }

    @Test
    public void shouldNotFoundUser_WhenSearchingByNotExistingUserId() {
        UserModel user = new UserModel(name, age, document);
        UserModel newUser = userRepository.save(user);
        newUser.setId(1000L);
        Optional<UserModel> foundUser = userRepository.findById(newUser.getId());
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void shouldFoundUser_WhenSearchingByExistingNameAndExistingDocument() {
        UserModel user = new UserModel(name, age, document);
        userRepository.save(user);
        Optional<UserModel> foundUser = userRepository.findByNameAndDocument(name, document);
        assertTrue(foundUser.isPresent());
        assertUserFields(foundUser.get());
    }

    @Test
    public void shouldNotFoundUser_WhenSearchingByNotExistingNameAndExistingDocument() {
        UserModel user = new UserModel(name, age, document);
        userRepository.save(user);
        Optional<UserModel> foundUser = userRepository.findByNameAndDocument("NOME ERRADO", document);
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void shouldNotFoundUser_WhenSearchingByExistingNameAndNotExistingDocument() {
        UserModel user = new UserModel(name, age, document);
        userRepository.save(user);
        Optional<UserModel> foundUser = userRepository.findByNameAndDocument(name, "DOCUMENTO_INVALIDO");
        assertTrue(foundUser.isEmpty());
    }

    private void assertUserNotFound() {
        List<UserModel> users = userRepository.findAll();
        assertEquals(0, users.size());
    }

    private void assertUserNotFound(Long id) {
        Optional<UserModel> userFound = userRepository.findById(id);
        assertTrue(userFound.isEmpty());
    }

    private void assertFirstUserFields() {
        List<UserModel> users = userRepository.findAll();
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

    private void assertUserFields(Long id, String pName, Integer pAge, String pDocumento) {
        Optional<UserModel> user = userRepository.findById(id);
        assertTrue(user.isPresent());
        assertEquals(pName, user.get().getName());
        assertEquals(pAge, user.get().getAge());
        assertEquals(pDocumento, user.get().getDocument());
    }

}

package br.com.bicmsystems.testesunitarios.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserModelTests {

	@Test
	public void shouldAssertTrue_WhenComparingFieldsUsingAllArgsContructor() {
		String name = "nome do usuario", document = "123.456.789-01";
		Integer age = 33;
		UserModel user = new UserModel(100L, name, age, document);
		assertNotNull(user);
		assertEquals(100L, user.getId());
		assertEquals(name, user.getName());
		assertEquals(age, user.getAge());
		assertEquals(document, user.getDocument());
		assertTrue(user.toString().contains(UserModel.class.getSimpleName()));
	}


	@Test
	public void shouldAssertTrue_WhenComparingFieldsUsingDeclaredContructor() {
		String name = "nome do usuario", document = "123.456.789-01";
		Integer age = 33;
		UserModel user = new UserModel(name, age, document);
		assertNotNull(user);
		assertEquals(name, user.getName());
		assertEquals(age, user.getAge());
		assertEquals(document, user.getDocument());
		assertTrue(user.toString().contains(UserModel.class.getSimpleName()));
	}
	

	@Test
	public void shouldAssertNull_WhenCreateUserWithEmptyContructor() {
		UserModel user = new UserModel();
		assertNull(user.getName());
		assertNull(user.getAge());
		assertNull(user.getDocument());
	}

}

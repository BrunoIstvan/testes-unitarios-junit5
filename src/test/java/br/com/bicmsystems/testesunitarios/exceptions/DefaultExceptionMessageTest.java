package br.com.bicmsystems.testesunitarios.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DefaultExceptionMessageTest {


	@Test
	public void shouldAssertTrue_WhenComparingFieldsUsingAllArgsContructor() {
		String test = "teste", outroTest = "outro-teste";
		DefaultExceptionMessage dem = new DefaultExceptionMessage(test);
		assertNotNull(dem);
		assertEquals(test, dem.getMessage());
		dem.setMessage(outroTest);
		assertEquals(outroTest, dem.getMessage());
	}


//	@Test
//	public void shouldAssertTrue_WhenComparingFieldsUsingDeclaredContructor() {
//		String name = "nome do usuario", document = "123.456.789-01";
//		Integer age = 33;
//		UserModel user = new UserModel(name, age, document);
//		assertNotNull(user);
//		assertEquals(name, user.getName());
//		assertEquals(age, user.getAge());
//		assertEquals(document, user.getDocument());
//		assertTrue(user.toString().contains(UserModel.class.getSimpleName()));
//	}
//	
//
//	@Test
//	public void shouldAssertNull_WhenCreateUserWithEmptyContructor() {
//		UserModel user = new UserModel();
//		assertNull(user.getName());
//		assertNull(user.getAge());
//		assertNull(user.getDocument());
//	}


}

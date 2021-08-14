package br.com.bicmsystems.testesunitarios.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class NotFoundException extends Exception {
   
	private static final long serialVersionUID = 1L;

	public NotFoundException(String ex) {
        super(ex);
    }
}

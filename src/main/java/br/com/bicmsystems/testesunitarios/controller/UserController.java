package br.com.bicmsystems.testesunitarios.controller;

import br.com.bicmsystems.testesunitarios.exceptions.DefaultExceptionMessage;
import br.com.bicmsystems.testesunitarios.exceptions.NotFoundException;
import br.com.bicmsystems.testesunitarios.model.UserModel;
import br.com.bicmsystems.testesunitarios.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserModel> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserModel findById(@PathVariable Long id) throws NotFoundException {
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserModel user) throws Exception {
    	
        try {
        	
			userService.save(user);
		    return new ResponseEntity<Void>(HttpStatus.CREATED);
		
		} catch (Exception e) {
			return new ResponseEntity<>("An error occured when save user data",
	                HttpStatus.INTERNAL_SERVER_ERROR);
		}
        
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserModel user) throws Exception {
        try {
            userService.update(user);
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(new DefaultExceptionMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occured when update user data",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        try {
            userService.delete(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(new DefaultExceptionMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occured when delete user data",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

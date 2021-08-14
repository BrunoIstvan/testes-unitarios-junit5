package br.com.bicmsystems.testesunitarios.service;

import br.com.bicmsystems.testesunitarios.exceptions.NotFoundException;
import br.com.bicmsystems.testesunitarios.model.UserModel;
import br.com.bicmsystems.testesunitarios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel save(UserModel user) throws Exception {
        return userRepository.save(user);
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    public void update(UserModel user) throws Exception {

        Optional<UserModel> existingUser = userRepository.findById(user.getId());
        if(existingUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.save(user);

    }

    public void delete(Long id) throws Exception {
    	
        Optional<UserModel> userFound = userRepository.findById(id);
        if(userFound.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.delete(userFound.get());
        
    }

    public UserModel findById(Long id) throws NotFoundException {
    	
        Optional<UserModel> userFound = userRepository.findById(id);
        if(userFound.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return userFound.get();
        
    }
}

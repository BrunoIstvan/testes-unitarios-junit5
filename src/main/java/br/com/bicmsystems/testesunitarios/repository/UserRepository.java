package br.com.bicmsystems.testesunitarios.repository;

import br.com.bicmsystems.testesunitarios.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByNameAndDocument(String name, String document);
}

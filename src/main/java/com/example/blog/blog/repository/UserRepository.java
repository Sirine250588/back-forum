package com.example.blog.blog.repository;

import com.example.blog.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //  Nous utilisons Optional pour vérifier la présence d'un élément facultatif (d'un élément qui peut ne pas exister) avant de l'utiliser.

    Optional<User> findByUserName(String username);

}

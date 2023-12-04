package com.example.schedular.repository;

import com.example.schedular.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    @Query(value = "select u from User u where email=:email and password=:password")
    Optional<User> findUserByUsernameAndPassword(String email, String password);

    @Query(value = "select u from User u where u.email = :email")
    Optional<User> findByUsername(String email);
    @Query(value = "select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

//    @Query(value="select u from User u where u.email=:email and token:token")
//    Optional<User> findByEmailAndToken(String email,String token);
}

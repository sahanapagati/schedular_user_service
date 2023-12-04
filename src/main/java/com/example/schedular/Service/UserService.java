package com.example.schedular.Service;


import com.example.schedular.repository.UserRepository;
import com.example.schedular.security.JwtHelper;
import com.example.schedular.user.JwtRequest;
import com.example.schedular.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@Service
public class UserService
    {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        JwtHelper jwtHelper;

        public ResponseEntity<String> signUp(User user)
        {
            try {
                Optional<User> exist=userRepository.findByEmail(user.getEmail());
                if(exist.isPresent())
                {
                    return ResponseEntity.badRequest().body("email already taken");
                }
                else {
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    return ResponseEntity.ok("User registration successful");
                }
            } catch (DataAccessException ex) {
                // Log the error or handle it as needed
                return ResponseEntity.badRequest().body("User registration failed: " + ex.getMessage());
            }
            }
        public void authenticate(UserDetails userDetails, JwtRequest jwtRequest)
        {
            System.out.println("raw :" +jwtRequest.getPassword()+ "hashd : "+userDetails.getPassword());
            boolean passwordCheck=passwordEncoder.matches(jwtRequest.getPassword(),userDetails.getPassword());
            System.out.println(passwordCheck);
            if (!passwordCheck)
            {
                throw new RuntimeException(" password not matching");
            }
        }

        public User updateProfile(User user)
        {

            User exist=userRepository.findByEmail(user.getUsername()).orElse(null);
            if(exist!=null)
            {
                user.setUser_id(exist.getUser_id());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
            else {
                throw new RuntimeException("User doesn't exist");
            }
            return exist;
        }
        public ResponseEntity<User> getProfile(String token)
        {

                String email = jwtHelper.getUsernameFromToken(token.substring(7));
                User exist = userRepository.findByEmail(email).orElse(null);
                return new ResponseEntity<>(exist, HttpStatus.UNAUTHORIZED);
        }


    }

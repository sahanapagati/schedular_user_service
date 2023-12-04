package com.example.schedular.controller;

import com.example.schedular.Service.UserService;
import com.example.schedular.repository.UserRepository;
import com.example.schedular.security.JwtHelper;
import com.example.schedular.user.JwtRequest;
import com.example.schedular.user.JwtResponse;
import com.example.schedular.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user)
    {
        return userService.signUp(user);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest)
    {

        UserDetails userDetails=userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        userService.authenticate(userDetails,jwtRequest);
        String token=this.jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse=JwtResponse.builder()
                .jwtToken(token).username(userDetails.getUsername()).build();
        return  new ResponseEntity<>(jwtResponse, HttpStatus.OK);

    }
    @PutMapping("/home/update_profile")
    public ResponseEntity<User> updateProfile(@RequestBody User user)
    {
        User updated=userService.updateProfile(user);
        return new ResponseEntity<>(updated,HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/home/get_profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String token)
    {
        return userService.getProfile(token);
    }
    @PostMapping("/api/data")
    public ResponseEntity<User> getData(@RequestHeader("Authorization") String token)
    {
//        String email=jwtHelper.getUsernameFromToken(token.substring(7));
//        if(jwtHelper.isTokenBelongsToUser(token,user.getUser_id()))
//        {
//            User user=userRepository.findByUsername(email).orElse(null);
//            return new ResponseEntity<User>(user, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(user,HttpStatus.UNAUTHORIZED);
        try {
            String email = jwtHelper.getUsernameFromToken(token.substring(7));
            User user = userRepository.findByUsername(email).orElse(null);

            if (jwtHelper.isTokenBelongsToUser(token,user.getUser_id()))
            {
                    return new ResponseEntity<>(user, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

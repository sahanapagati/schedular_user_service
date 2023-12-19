package com.example.schedular.controller;

import com.example.schedular.Service.UserDetailsServiceImpl;
import com.example.schedular.Service.UserService;
import com.example.schedular.security.JwtHelper;
import com.example.schedular.user.JwtRequest;
import com.example.schedular.user.JwtResponse;
import com.example.schedular.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;
    @Mock
    JwtHelper jwtHelper;
    @Mock
    UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSignUp()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User user1 = new User(1L, "virat", "kohli", "male", "virat@gmail.com", 26996585352L, "virat@123");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("User registration successfull",HttpStatus.OK);

        // Act
        when(userService.signUp(user1)).thenReturn(responseEntity);
        ResponseEntity<String> response = userController.signUp(user1);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo("User registration successfull");

    }

    @Test
    void testLogin()
    {
        JwtRequest jwtRequest = new JwtRequest("virat@gmail.com", "virat@123");

        User userDetails = new User(1L, "virat", "kohli", "male", "virat@gmail.com", 26996585352L, "virat@123");
        when(userDetailsServiceImpl.loadUserByUsername(jwtRequest.getEmail())).thenReturn(userDetails);

        doNothing().when(userService).authenticate(userDetails, jwtRequest);

        String token = "your_generated_token";
        when(jwtHelper.generateToken(userDetails)).thenReturn(token);

        ResponseEntity<JwtResponse> expectedResponse = new ResponseEntity<>(
                JwtResponse.builder().jwtToken(token).username(userDetails.getUsername()).build(),
                HttpStatus.OK
        );

        // Act
        ResponseEntity<JwtResponse> actualResponse = userController.login(jwtRequest);

        // Assert
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedResponse.getBody());

        // Verify that methods were called
        verify(userDetailsServiceImpl, times(1)).loadUserByUsername(jwtRequest.getEmail());
        verify(userService, times(1)).authenticate(userDetails, jwtRequest);
        verify(jwtHelper, times(1)).generateToken(userDetails);
    }



    @Test
    void testUpdateProfile()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        User user1 = new User(1L, "virat", "kohli", "male", "virat@gmail.com", 26996585352L, "kohli@123");
        ResponseEntity<User> responseEntity1 = new ResponseEntity<>(user1, HttpStatus.OK);

        // Act
        when(userService.updateProfile(user1)).thenReturn(user1);

        ResponseEntity<User> response = userController.updateProfile(user1);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody()).isEqualTo(user1);

        // Verify that the updateProfile method was called with the correct argument
//        verify(userController, times(1)).updateProfile(eq(user1));
    }

    @Test
    void getProfile()
    {

    }
}
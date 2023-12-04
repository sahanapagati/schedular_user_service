package com.example.SchedularApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    private Long user_id;
    private String first_name;
    private String last_name;
    private String gender;
    private String email;
    private Long mobile;
    private String password;
    private String address;
}
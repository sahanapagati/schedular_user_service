package com.example.schedular.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest
{
    private String email;
    private String password;

}
// "first_name":"rohit",
//         "last_name":"sharma",
//         "gender":"male",
//         "email":"rohit@gmail.com",
//         "mobile":54764552554,
//         "password":"rohit@123",
//         "address":"bnglr"

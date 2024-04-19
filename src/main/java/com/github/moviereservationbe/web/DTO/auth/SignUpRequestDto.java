package com.github.moviereservationbe.web.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String name;
    private String myId;
    private String password;
    private Date birthday;
    private String phoneNumber;
}

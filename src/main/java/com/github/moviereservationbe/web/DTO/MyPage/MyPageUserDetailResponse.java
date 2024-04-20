package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserDetailResponse {
    private String name;
    private String myId;
    private Date birthday;
    private String phoneNumber;
    private String password;
}

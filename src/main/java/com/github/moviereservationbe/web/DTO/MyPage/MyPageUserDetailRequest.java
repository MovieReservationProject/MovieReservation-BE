package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserDetailRequest {
    private String phoneNumber;
    private String password;
}

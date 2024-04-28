package com.github.moviereservationbe.web.DTO.MyPage;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageUserDetailResponse {
    private String name;
    private String myId;
    private Date birthday;
    private String phoneNumber;
//    private String password;
}

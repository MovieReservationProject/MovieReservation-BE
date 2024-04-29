package com.github.moviereservationbe.web.DTO.MyPage;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String phoneNumber;
//    private String password;
}

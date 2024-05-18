package com.github.moviereservationbe.web.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserInfoDto {
    private Long id;
    private String nickname;
    private String email;


}

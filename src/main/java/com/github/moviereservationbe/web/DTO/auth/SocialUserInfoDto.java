package com.github.moviereservationbe.web.DTO.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialUserInfoDto {
    private Long id;
    private String nickname;
    private String email;

    public SocialUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}

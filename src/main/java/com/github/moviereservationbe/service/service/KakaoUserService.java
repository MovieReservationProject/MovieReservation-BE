package com.github.moviereservationbe.service.service;

import com.github.moviereservationbe.web.DTO.auth.SocialUserInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoUserService {
    public SocialUserInfoDto kakaoLogin(String code, HttpServletResponse response) {
    }
}

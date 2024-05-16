package com.github.moviereservationbe.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.moviereservationbe.service.service.KakaoUserService;
import com.github.moviereservationbe.web.DTO.auth.SocialUserInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {
    private final KakaoUserService kakaoUserService;

    public SocialUserInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException{
        return kakaoUserService.kakaoLogin(code, response);
    }
}

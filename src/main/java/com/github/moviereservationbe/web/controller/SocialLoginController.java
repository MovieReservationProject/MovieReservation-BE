package com.github.moviereservationbe.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.moviereservationbe.service.service.KakaoUserService;
import com.github.moviereservationbe.web.DTO.auth.SocialUserInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {
    private final KakaoUserService kakaoUserService;

    // 카카오 로그인
    @GetMapping("/oauth/kakao") //redirect uri
    public SocialUserInfoDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException{
        //code: 카카오 서버로부터 받은 인가 코드
        return kakaoUserService.kakaoLogin(code, response);
    }
}

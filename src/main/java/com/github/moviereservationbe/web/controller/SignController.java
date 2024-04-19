package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.service.service.AuthService;
import com.github.moviereservationbe.web.DTO.auth.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/auth")
public class SignController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public String register(@RequestBody SignUpRequestDto signUpRequestDto){
        boolean isSuccess= authService.signUp(signUpRequestDto);
        return isSuccess ? "회원가입 성공" : "회원가입 실패";
    }
}

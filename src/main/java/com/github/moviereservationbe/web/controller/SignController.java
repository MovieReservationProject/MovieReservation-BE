package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.service.service.AuthService;
import com.github.moviereservationbe.web.DTO.ResponseDto;
import com.github.moviereservationbe.web.DTO.auth.FindIdPasswordDto;
import com.github.moviereservationbe.web.DTO.auth.LoginRequestDto;
import com.github.moviereservationbe.web.DTO.auth.SignUpRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/auth")
public class SignController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseDto register(@RequestBody SignUpRequestDto signUpRequestDto){
        boolean isSuccess= authService.signUp(signUpRequestDto);
        if(isSuccess) return new ResponseDto(HttpStatus.OK.value(), "SignUp successful");
        else return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "SignUp fail");
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){
        String token= authService.login(loginRequestDto);
        httpServletResponse.setHeader("Token", token);
        return new ResponseDto(HttpStatus.OK.value(), "Login Success");
    }

    @PostMapping("/logout")
    public ResponseDto logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        boolean isSuccess= authService.logout(httpServletRequest, httpServletResponse);
        if(isSuccess) return new ResponseDto(HttpStatus.OK.value(), "Logout successful" );
        else return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "Logout fail");
    }

    @GetMapping("/findId")
    public ResponseDto findId(@RequestBody FindIdPasswordDto findIdPasswordDto){
        return authService.findId(findIdPasswordDto);
    }

    @PutMapping("/getPassword")
    public ResponseDto getNewPassword(@RequestBody FindIdPasswordDto findIdPasswordDto){
        return authService.getNewPassword(findIdPasswordDto);
    }
}

package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.repository.Auth.user.User;
import com.github.moviereservationbe.repository.Auth.user.UserJpa;
import com.github.moviereservationbe.repository.Auth.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@RequiredArgsConstructor
public class test {
    private final UserJpa userJpa;

    @GetMapping("/test")
    public String test() {
        User user = userJpa.findById(1).orElseThrow(() -> new NotFoundException("ss"));

        Integer userId = user.getUserId();

        return "test success: " + userId;
    }

    @GetMapping("/test2")
    public String test2(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return "test success, userId: " + customUserDetails.getUserId();
    }
}

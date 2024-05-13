package com.github.moviereservationbe.web.controller;

import com.github.moviereservationbe.service.service.EmailCertificationService;
import com.github.moviereservationbe.service.service.RedisUtil;
import com.github.moviereservationbe.web.DTO.redis.EmailRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class EmailCertificationController {
    private final EmailCertificationService emailCertificationService;
    private final RedisUtil redisUtil;

    @PostMapping("/send-mail")
    public String sendMail(@RequestBody @Valid EmailRequest emailRequest){
        System.out.println("이메일 인증 이메일 : " + emailRequest.getEmail());
       return emailCertificationService.joinEmail(emailRequest.getEmail());
    }

}

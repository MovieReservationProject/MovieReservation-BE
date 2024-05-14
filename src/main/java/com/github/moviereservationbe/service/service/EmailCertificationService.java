package com.github.moviereservationbe.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.github.moviereservationbe.config.redis.EmailCertificationConfig.generateRandomNumber;

@Service
@RequiredArgsConstructor
public class EmailCertificationService {
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    private final int authNumber= generateRandomNumber(100000, 999999);
    @Value("${email.address")
    private String emailAddress;

    public String joinEmail(String email) {
        String setFrom= emailAddress;
        String toMail= email;
        String title= "[인증] 영화 예매 사이트 가입 인증번호";
        String content=
                "회원가입 창으로 돌아가 인증 번호를 정확히 입력해주세요." +
                    "<br><br>" +
                    "인증 번호는 " + authNumber + "입니다." +
                    "<br>" ;
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    @Transactional
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message= mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper= new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        }catch(MessagingException e){
            e.printStackTrace();
        }

    }

    public Boolean checkAuthNum(String email, String authNum) {
        if(redisUtil.getData(authNum) == null ) return false;
        else if(redisUtil.getData(authNum).equals(email)) return true;
        else return false;
    }
}

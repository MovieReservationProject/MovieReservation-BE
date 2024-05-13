package com.github.moviereservationbe.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.Random;

@Configuration
public class EmailCertificationConfig {
    @Value("${email.address")
    private String emailAddress;
    @Value("${email.app-password")
    private String appPassword;

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender= new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailAddress);
        mailSender.setPassword(appPassword);

        Properties javaMailProperties= new Properties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.naver.com");
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    public static int generateRandomNumber(int min, int max){
        Random random= new Random();
        return random.nextInt(max-min+1) +min;
    }

}

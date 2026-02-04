package edu.malaka96.authify_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String formEmail;

    public void sendWelcomeEmail(String toEmail, String name){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(formEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Our Platform");
            message.setText("Hello "+name+"\n\nThanks for registering with us! \n\nRegards, \nAuthify Team");

            mailSender.send(message);
        } catch (MailException e) {
            System.out.println(e.getMessage());
        }

    }
}

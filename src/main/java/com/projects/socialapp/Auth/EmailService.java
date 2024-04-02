package com.projects.socialapp.Auth;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendResetPasswordEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Myapp" + " <" + "manager@ex.com" + ">");
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Please click the following link to reset your password: http://localhost:8080/api/auth/reset?token=" + resetToken);
        emailSender.send(message);
        System.out.println("Email sent successfully!");

    }

}
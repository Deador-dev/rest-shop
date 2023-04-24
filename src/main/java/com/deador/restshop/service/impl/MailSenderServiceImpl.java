package com.deador.restshop.service.impl;

import com.deador.restshop.model.User;
import com.deador.restshop.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {
    private static final String MESSAGE_SENDING_ERROR = "Cannot send a message to an empty email or with an invalid verification code";
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sendFromAddress;
    @Value("${baseURL}")
    private String baseURL;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendVerificationMessage(User user) {
        String toEmail = user.getEmail();
        String subject = "Verification code!";
        String verifyURL = baseURL + "/verify?code=" + user.getVerificationCode();

        String message = String.format(
                "Hello, %s %s.\n" +
                        "Welcome to the shop! Please visit the following link to verify your account: %s",
                user.getFirstName(),
                user.getLastName(),
                verifyURL
        );

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sendFromAddress);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
        log.debug("Email has been sent to '{}'", user.getEmail());
    }
}

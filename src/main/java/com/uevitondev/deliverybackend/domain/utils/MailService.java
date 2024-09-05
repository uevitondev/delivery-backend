package com.uevitondev.deliverybackend.domain.utils;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    public record EmailDTO(String email, String subject, String title, String text, String templateName) {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String mailHost;
    private final JavaMailSender javaMailSender;


    public MailService(JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendEmail(EmailDTO emailDto) {
        try {
            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, "utf-8");
            var mailTemplate = buildMailTemplate(emailDto);
            helper.setFrom(mailHost);
            helper.setTo(emailDto.email);
            helper.setSubject(emailDto.subject);
            helper.setText(mailTemplate, true);
            javaMailSender.send(mimeMessage);
            LOGGER.info("send email for mail template: {}", emailDto.templateName);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email in mail service: {}", e.getMessage());
            throw new IllegalStateException("failed to send email in mail service");
        }
    }

    public String buildMailTemplate(EmailDTO emailDto) {
        LOGGER.info("build mail template: {}", emailDto.templateName);
        var template = loadTemplate(emailDto.templateName);
        template = template.replace("#{{title}}", emailDto.title);
        template = template.replace("#{{subject}}", emailDto.subject);
        template = template.replace("#{{text}}", emailDto.text);
        return template;
    }


    public String loadTemplate(String templateName) {
        try {
            var resource = new ClassPathResource("templates/" + templateName);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("failed to load template: {}", templateName);
            throw new IllegalStateException("failed to load template in mail service");
        }
    }


}

package com.uevitondev.deliverybackend.domain.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

    @Value("${spring.mail.username}")
    private String mailHost;
    private final JavaMailSender javaMailSender;
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

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
            logger.info("[MailService:sendEmail] send email (template: {}), (subject: {}), (email: {})",
                    emailDto.templateName, emailDto.subject, emailDto.email)
            ;
        } catch (MessagingException e) {
            logger.error("failed to send email in mail service: {}", e.getMessage());
            throw new IllegalStateException("failed to send email in mail service");
        }
    }

    public String buildMailTemplate(EmailDTO emailDto) {
        logger.info("[MailService:buildMailTemplate] build template (template: {}) for to: {}",
                emailDto.templateName, emailDto.subject
        );
        var template = loadTemplate(emailDto.templateName);
        template = template.replace("#{{title}}", emailDto.title);
        template = template.replace("#{{subject}}", emailDto.subject);
        template = template.replace("#{{text}}", emailDto.text);
        return template;
    }


    public String loadTemplate(String templateName) {
        try {
            var resource = new ClassPathResource("templates/"+ templateName);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("[MailService:loadTemplate] failed to load template in mail service: {}", e.getMessage());
            throw new IllegalStateException("failed to load template in mail service");
        }
    }




}

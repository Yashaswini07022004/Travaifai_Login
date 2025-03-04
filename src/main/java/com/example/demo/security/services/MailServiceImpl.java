package com.example.demo.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.model.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(Mail mail) {
        // Validate mail fields
        if (mail.getMailSubject() == null || mail.getMailSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject must not be null or empty");
        }
        if (mail.getMailFrom() == null || mail.getMailFrom().isEmpty()) {
            throw new IllegalArgumentException("From address must not be null or empty");
        }
        if (mail.getMailTo() == null || mail.getMailTo().isEmpty()) {
            throw new IllegalArgumentException("To address must not be null or empty");
        }
        if (mail.getMailContent() == null || mail.getMailContent().isEmpty()) {
            throw new IllegalArgumentException("Content must not be null or empty");
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setTo(mail.getMailTo().split(","));
            mimeMessageHelper.setText(mail.getMailContent());

            if (mail.getMailCc() != null && !mail.getMailCc().isEmpty()) {
                mimeMessageHelper.setCc(mail.getMailCc().split(","));
            }
            if (mail.getMailBcc() != null && !mail.getMailBcc().isEmpty()) {
                mimeMessageHelper.setBcc(mail.getMailBcc().split(","));
            }

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom exception
        }
    }

}
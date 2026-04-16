package com.wuubangdev.portfolio.infrastructure.global.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendContactNotification(String adminEmail, String contactName, String contactEmail, String subject, String message) {
        String emailSubject = "New Contact Form Submission: " + subject;
        String emailText = String.format(
                "You have received a new contact form submission.\n\n" +
                "Name: %s\n" +
                "Email: %s\n" +
                "Subject: %s\n" +
                "Message:\n%s",
                contactName, contactEmail, subject, message
        );
        sendEmail(adminEmail, emailSubject, emailText);
    }
}

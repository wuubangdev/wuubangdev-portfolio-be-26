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

    public void sendContactNotificationToAdmin(String adminEmail, String subject, String text) {
        sendEmail(adminEmail, subject, text);
    }

    public void sendContactAutoReply(String to, String subject, String text) {
        sendEmail(to, subject, text);
    }

    public void sendAccountActivationEmail(String to, String subject, String text) {
        sendEmail(to, subject, text);
    }

    public void sendResetPasswordEmail(String to, String username, String resetPasswordLink) {
        String subject = "Reset your password";
        String text = String.format(
                "Hi %s,\n\n" +
                        "We received a request to reset your password.\n" +
                        "Use the link below to set a new password:\n%s\n\n" +
                        "If you did not request this, you can ignore this email.",
                username, resetPasswordLink
        );
        sendEmail(to, subject, text);
    }
}

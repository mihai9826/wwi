package org.mihaimadan.wwi.users.service.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetListener implements ApplicationListener<PasswordResetCompleteEvent> {
    @Value("${app.url}")
    private String appUrl;

    private final JavaMailSender mailSender;

    public PasswordResetListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void onApplicationEvent(PasswordResetCompleteEvent passwordResetEvent) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(passwordResetEvent.getEmail());
        mailMessage.setSubject("You requested password reset");
        mailMessage.setText("To complete password reset please access: " +
                appUrl + "/auth/reset-password?token=" + passwordResetEvent.getToken());

        mailSender.send(mailMessage);
    }
}

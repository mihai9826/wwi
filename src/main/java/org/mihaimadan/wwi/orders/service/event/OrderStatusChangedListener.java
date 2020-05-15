package org.mihaimadan.wwi.orders.service.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusChangedListener implements ApplicationListener<OrderStatusChangedEvent> {

    @Value("${app.url}")
    private String appUrl;

    private final JavaMailSender mailSender;

    public OrderStatusChangedListener(JavaMailSender mailSender) { this.mailSender = mailSender; }

    @Async
    @Override
    public void onApplicationEvent(OrderStatusChangedEvent event) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(event.getEmail());
        mailMessage.setSubject("Your order #" + event.getOrderId().toString() + "has been updated");
        mailMessage.setText("Your order status has changed to " + event.getStatus() + "." +
                " Please visit " + appUrl + "/client/orders");

        mailSender.send(mailMessage);
    }
}

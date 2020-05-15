package org.mihaimadan.wwi.orders.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderStatusChangedEvent extends ApplicationEvent {
    private String email;
    private String status;
    private Long orderId;

    public OrderStatusChangedEvent(String email, String status, Long orderId) {
        super(email);
        this.email = email;
        this.status = status;
        this.orderId = orderId;
    }
}

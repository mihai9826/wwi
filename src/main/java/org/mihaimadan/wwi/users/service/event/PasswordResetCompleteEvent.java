package org.mihaimadan.wwi.users.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PasswordResetCompleteEvent extends ApplicationEvent {
    private String email;
    private String token;

    public PasswordResetCompleteEvent(String email, String token) {
        super(email);
        this.email = email;
        this.token = token;
    }
}

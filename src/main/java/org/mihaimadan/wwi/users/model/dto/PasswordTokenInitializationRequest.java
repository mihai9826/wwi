package org.mihaimadan.wwi.users.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordTokenInitializationRequest {
    private String email;
}

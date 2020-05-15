package org.mihaimadan.wwi.users.model.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fullName;
    private String emailAddress;
    private String deliveryAddress;
    private String phoneNumber;
    private String password;
    private String role;
}

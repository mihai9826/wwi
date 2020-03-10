package org.mihaimadan.wwi.users.model;

import lombok.Data;

@Data
public class UserDTO {

    private Long personId;

    private String fullName;

    private String emailAddress;

    private String phoneNumber;

    private String password;

    private String role;
}

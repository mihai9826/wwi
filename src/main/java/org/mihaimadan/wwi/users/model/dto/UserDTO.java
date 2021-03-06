package org.mihaimadan.wwi.users.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long personId;

    private String fullName;

    private String emailAddress;

    private String deliveryAddress;

    private String password;

    private String phoneNumber;

    private String role;
}

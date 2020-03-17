package org.mihaimadan.wwi.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long personId;

    private String fullName;

    private String emailAddress;

    private String password;

    private String phoneNumber;

    private String role;
}

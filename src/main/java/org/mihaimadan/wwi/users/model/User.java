package org.mihaimadan.wwi.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "People", schema = "Application")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @NotNull
    private String fullName;

    @NotNull
    private String emailAddress;

    private String deliveryAddress;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String password;

    private boolean isClient;

    private boolean isAdmin;

    public void setRole(String role) {
        this.isClient = role.equals("CLIENT");
        this.isAdmin = role.equals("ADMIN");
    }

    public String getRole() {
        return this.isAdmin ? "ADMIN" : "CLIENT";
    }
}

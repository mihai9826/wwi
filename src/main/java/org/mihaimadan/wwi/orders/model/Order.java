package org.mihaimadan.wwi.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mihaimadan.wwi.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders", schema = "Sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToOne
    @JoinColumn(name = "ContactPersonID")
    private User contactPerson;

    @OneToMany(mappedBy = "theOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @NotNull
    private double orderValue;

    private String comments;

    @NotNull
    private String deliveryAddress;

    @NotNull
    private LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        orderDate = LocalDateTime.now();
    }
}

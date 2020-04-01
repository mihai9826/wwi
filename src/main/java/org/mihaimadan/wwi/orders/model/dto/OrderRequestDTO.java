package org.mihaimadan.wwi.orders.model.dto;

import lombok.Data;
import org.mihaimadan.wwi.users.model.UserDTO;

import java.util.List;

@Data
public class OrderRequestDTO {
    private UserDTO contactPerson;
    private List<OrderLineRequestDTO> orderLines;
    private String comments;
    private String deliveryAddress;
    private double orderValue;
}

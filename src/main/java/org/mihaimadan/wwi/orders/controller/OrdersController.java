package org.mihaimadan.wwi.orders.controller;

import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OrdersController {

    private final OrderRepository orderRepository;

    public OrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "not found by given id"));
    }
}

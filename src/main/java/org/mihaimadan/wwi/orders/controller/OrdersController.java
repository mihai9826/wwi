package org.mihaimadan.wwi.orders.controller;

import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.orders.model.dto.OrderRequestDTO;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.mihaimadan.wwi.orders.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OrdersController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrdersController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found by given id"));
    }

    @GetMapping("/admin/orders/pending-processing")
    public List<Order> getPendingAndProcessingOrders(@RequestParam(required = false) String date,
                                                     @RequestParam(required = false) String status) {
        return orderService.getPendingAndProcessingOrders(date, status);
    }

    @GetMapping("/admin/orders/{id}/pending-processing")
    public Order getPendingAndProcessingOrdersOfId(@PathVariable Long id) {
        return orderService.getPendingAndProcessingOrdersOfId(id);
    }

    @GetMapping("/admin/orders/dispatched")
    public Page<Order> findPaginateDispatchedOrders(@RequestParam int page, @RequestParam int size) {
        return orderService.findDispatchedOrdersPaginate(page, size);
    }

    @GetMapping("/admin/orders/{id}/dispatched")
    public Order getDispatchedOrderOfId(@PathVariable Long id) {
        return orderService.getDispatchedOrderOfId(id);
    }

    @GetMapping("/client/{id}/orders")
    public List<Order> getClientOrders(@PathVariable Long id) {
        return orderService.getClientOrders(id);
    }

    @PostMapping("/client/orders")
    public void createNewOrder(@RequestBody OrderRequestDTO orderRequest) {
        orderService.createNewOrder(orderRequest);
    }
}

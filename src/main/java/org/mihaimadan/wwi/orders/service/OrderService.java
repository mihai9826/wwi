package org.mihaimadan.wwi.orders.service;

import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.orders.model.OrderLine;
import org.mihaimadan.wwi.orders.model.dto.OrderRequestDTO;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.mihaimadan.wwi.users.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    public void createNewOrder(@RequestBody OrderRequestDTO orderRequest) {
        Order newOrder = new Order();

        orderRequest.getOrderLines().forEach(it -> {
            OrderLine orderLine = modelMapper.map(it, OrderLine.class);
            newOrder.addOrderLine(orderLine);
        });

        BeanUtils.copyProperties(orderRequest, newOrder, "contactPerson", "orderLines");

        User contactPerson = modelMapper.map(orderRequest.getContactPerson(), User.class);

        newOrder.setContactPerson(contactPerson);

        orderRepository.save(newOrder);//80000 last id
    }
}

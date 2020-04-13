package org.mihaimadan.wwi.orders.service;

import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.orders.model.OrderLine;
import org.mihaimadan.wwi.orders.model.dto.OrderRequestDTO;
import org.mihaimadan.wwi.orders.repository.OrderRepository;
import org.mihaimadan.wwi.orders.service.event.OrderStatusChangedEvent;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public OrderService(ApplicationEventPublisher eventPublisher, OrderRepository orderRepository,
                        UserService userService, ModelMapper modelMapper) {
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
        this.userService = userService;
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

    public Order getOrderByIdForAdmin(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found by given id"));
    }

    public List<Order> getPendingAndProcessingOrders(String date, String status) {
        if (date != null && status != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);
            return orderRepository.findAllByStatusAndOrderDateAfterOrderByOrderDateDesc(status, parsedDate);
        }
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);
            return orderRepository.findAllByOrderDateAfterOrderByOrderDateDesc(parsedDate);
        }
        if (status != null) {
            return orderRepository.findAllByStatusOrderByOrderDateDesc(status);
        }

        return orderRepository
                .findAllByStatusNotOrderByOrderDateDesc("DISPATCHED");
    }

    public Order getPendingAndProcessingOrdersOfId(Long id) {
        return orderRepository
                .findAllByStatusNotOrderByOrderDateDesc("DISPATCHED").stream()
                .filter(it -> it.getOrderId().equals(id))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found with given id"));
    }

    public List<Order> getClientOrders(Long id) {
        User client = userService.getById(id);
        return orderRepository.findAllByContactPersonOrderByOrderDateDesc(client)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client doesn't have orders"));
    }

    public Page<Order> findDispatchedOrdersPaginate(int page, int size) {
        return orderRepository.findAllByStatusOrderByOrderDateDesc("DISPATCHED", PageRequest.of(page, size));
    }

    public Order getDispatchedOrderOfId(Long id) {
        return orderRepository.findByStatusAndOrderId("DISPATCHED", id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispatched order with" +
                        " given id not found"));
    }

    public Order changeOrderStatus(Long id, String newStatus) {
        Order orderToBeUpdated = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id not found"));

        orderToBeUpdated.setStatus(newStatus);

        orderRepository.save(orderToBeUpdated);

        String email = orderToBeUpdated.getContactPerson().getEmailAddress();
        eventPublisher.publishEvent(new OrderStatusChangedEvent(email, newStatus, id));

        return orderToBeUpdated;
    }
}

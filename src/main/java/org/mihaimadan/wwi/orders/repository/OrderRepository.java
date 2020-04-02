package org.mihaimadan.wwi.orders.repository;

import org.mihaimadan.wwi.orders.model.Order;
import org.mihaimadan.wwi.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findAllByContactPersonOrderByOrderIdDesc(User contactPerson);
}

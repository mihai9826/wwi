package org.mihaimadan.wwi.orders.repository;

import org.mihaimadan.wwi.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

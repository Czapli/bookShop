package org.czaplinski.bookshop.order.db;

import org.czaplinski.bookshop.order.domain.Order;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime timeStamp);
}

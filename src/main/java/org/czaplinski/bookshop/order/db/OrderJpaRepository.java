package org.czaplinski.bookshop.order.db;

import org.czaplinski.bookshop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}

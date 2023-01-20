package org.czaplinski.bookshop.order.application.port;

import lombok.Value;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.order.domain.Order;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.czaplinski.bookshop.order.domain.Recipient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);


    @Value
    class RichOrder{
        Long id;
        OrderStatus status;
        Set<OrderItem> items;
        Recipient recipient;
        LocalDateTime createAt;

        public BigDecimal totalPrice(){
            return items.stream()
                    .map(item ->
                            item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
    }
}

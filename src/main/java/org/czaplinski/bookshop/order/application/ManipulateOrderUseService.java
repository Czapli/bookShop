package org.czaplinski.bookshop.order.application;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase;
import org.czaplinski.bookshop.order.db.OrderJpaRepository;
import org.czaplinski.bookshop.order.domain.Order;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManipulateOrderUseService implements ManipulateOrderUseCase {
    private final OrderJpaRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order
                .builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        repository.findById(id)
                .ifPresent(order -> {
                    order.updateStatus(status);
                    repository.save(order);
                });
    }

    @Override
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }


}

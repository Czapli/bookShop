package org.czaplinski.bookshop.order.application;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.catalog.db.BookJpaRepository;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase;
import org.czaplinski.bookshop.order.db.OrderJpaRepository;
import org.czaplinski.bookshop.order.db.RecipientJpaRepository;
import org.czaplinski.bookshop.order.domain.Order;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.czaplinski.bookshop.order.domain.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ManipulateOrderUseService implements ManipulateOrderUseCase {
    private final OrderJpaRepository repository;
    private final BookJpaRepository bookRepository;
    private final RecipientJpaRepository recipientRepository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Set<OrderItem> items = command
                .getItems()
                .stream()
                .map(this::toOrderItem)
                .collect(Collectors.toSet());
        Order order = Order
                .builder()
                .recipient(getOrCreateRecipient(command.getRecipient()))
                .items(items)
                .build();
        Order save = repository.save(order);
        bookRepository.saveAll(updateBooks(items));
        return PlaceOrderResponse.success(save.getId());
    }

    private Recipient getOrCreateRecipient(Recipient recipient) {
        return recipientRepository
                .findByEmailIgnoreCase(recipient.getEmail())
                .orElse(recipient);
    }

    private Set<Book> updateBooks(Set<OrderItem> items) {
        return items.stream()
                .map(item -> {
                    Book book = item.getBook();
                    book.setAvailable(book.getAvailable() - item.getQuantity());
                    return book;
                }).collect(Collectors.toSet());
    }

    private OrderItem toOrderItem(OrderItemCommand orderItemCommand) {
        Book book = bookRepository.getReferenceById(orderItemCommand.getBookId());
        int quantity = orderItemCommand.getQuantity();
        if (book.getAvailable() >= quantity) {
            return new OrderItem(book, orderItemCommand.getQuantity());
        }
        throw new IllegalArgumentException("Too many copies of book " + book.getId() + " requested " + quantity + " of " + book.getAvailable() + " available");
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

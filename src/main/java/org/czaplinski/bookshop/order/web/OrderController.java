package org.czaplinski.bookshop.order.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.czaplinski.bookshop.CreatedURI;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import org.czaplinski.bookshop.order.application.port.QueryOrderUseCase;
import org.czaplinski.bookshop.order.application.port.QueryOrderUseCase.RichOrder;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.czaplinski.bookshop.order.domain.Recipient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final ManipulateOrderUseCase manipulateOrder;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    public List<RichOrder> getAll() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RichOrder> getOrderById(@PathVariable Long id) {
        return queryOrder.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@RequestBody CreateOrderCommand command) {
        return manipulateOrder
                .placeOrder(command.toPlaceOrderCommand())
                .handle(
                        orderId -> ResponseEntity.created(orderUri(orderId)).build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    private static URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrderStatus(@PathVariable Long id, @RequestBody UpdateStatusCommand command) {
        OrderStatus orderStatus = OrderStatus
                .parseString(command.status)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + command.status));
        manipulateOrder.updateOrderStatus(id, orderStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        manipulateOrder.deleteOrderById(id);
    }

    @Data
    static class CreateOrderCommand {
        List<OrderItemCommand> items;
        RecipientCommand recipient;

        PlaceOrderCommand toPlaceOrderCommand() {
            List<OrderItem> orderItems = items.stream()
                    .map(item -> new OrderItem(item.bookId, item.quantity))
                    .collect(Collectors.toList());
            return new PlaceOrderCommand(orderItems, recipient.toRecipient());
        }

    }

    @Data
    static class OrderItemCommand {
        Long bookId;
        int quantity;
    }

    @Data
    static class RecipientCommand {
        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;

        Recipient toRecipient() {
            return new Recipient(name, phone, street, city, zipCode, email);
        }
    }

    @Data
    static class UpdateStatusCommand {
        String status;
    }
}
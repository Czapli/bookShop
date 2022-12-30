package org.czaplinski.bookshop.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.czaplinski.bookshop.commons.Either;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.OrderStatus;
import org.czaplinski.bookshop.order.domain.Recipient;

import java.util.List;

public interface ManipulateOrderUseCase {
    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void updateOrderStatus(Long id, OrderStatus orderStatus);

    void deleteOrderById(Long id);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;
    }

    @Value
    class PlaceOrderResponse extends Either<String, Long> {
        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }

    }
}

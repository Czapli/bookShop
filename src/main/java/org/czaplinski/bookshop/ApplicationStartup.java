package org.czaplinski.bookshop;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase.CreateBookCommand;
import org.czaplinski.bookshop.catalog.db.AuthorsJpaRepository;
import org.czaplinski.bookshop.catalog.domain.Author;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase;
import org.czaplinski.bookshop.order.application.port.QueryOrderUseCase;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

import static org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase.*;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {
    private final CatalogUseCase catalog;
    private final ManipulateOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorsJpaRepository authorsRepository;

    @Override
    public void run(String... args) throws Exception {
        initData();
        placeOrder();
    }

    private void placeOrder() {
        Book effectiveJava = catalog.findOneByTitle("Effective Java").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        Book javaPuzzelrs = catalog.findOneByTitle("Java Puzzelrs").orElseThrow(() -> new IllegalStateException("Cannot find book"));
        Recipient recipient = Recipient
                .builder()
                .name("Janusz")
                .phone("123123123")
                .street("Warszawska")
                .zipCode("71-425")
                .city("Warszawa")
                .email("janusz@test.org")
                .build();
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(effectiveJava.getId(), 16))
                .item(new OrderItem(javaPuzzelrs.getId(), 7))
                .build();
        PlaceOrderResponse response = placeOrder.placeOrder(command);
        String result = response.handle(
                orderId -> "Created ORDER with id: " + orderId,
                error -> "Filed created order " + error);

        queryOrder.findAll()
                .forEach(order -> System.out.println("got order with total price: " + order.totalPrice() + " Details: " + order));
    }

    private void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorsRepository.save(joshua);
        authorsRepository.save(neal);

        CreateBookCommand effectiveJava = new CreateBookCommand("Effective java", Set.of(joshua.getId()), 2005, new BigDecimal("79.00"));
        CreateBookCommand javaPuzzelrs = new CreateBookCommand("java Puzzelrs", Set.of(joshua.getId(), neal.getId()), 2018, new BigDecimal("99.00"));
        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzelrs);
    }
}

package org.czaplinski.bookshop.catalog.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czaplinski.bookshop.catalog.application.CatalogInitializerService;
import org.czaplinski.bookshop.catalog.application.port.CatalogInitializerUseCase;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase;
import org.czaplinski.bookshop.catalog.db.AuthorsJpaRepository;
import org.czaplinski.bookshop.catalog.domain.Author;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase;
import org.czaplinski.bookshop.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import org.czaplinski.bookshop.order.application.port.QueryOrderUseCase;
import org.czaplinski.bookshop.order.domain.OrderItem;
import org.czaplinski.bookshop.order.domain.Recipient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final CatalogInitializerUseCase initializer;

    @PostMapping("/initialization")
    public void initialize() {
        initializer.initialize();
    }

}

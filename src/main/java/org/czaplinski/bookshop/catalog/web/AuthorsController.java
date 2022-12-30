package org.czaplinski.bookshop.catalog.web;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.catalog.application.port.AuthorsUseCase;
import org.czaplinski.bookshop.catalog.domain.Author;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorsController {
    private final AuthorsUseCase authors;
@GetMapping
    public List<Author> findAll() {
        return authors.findAll();
    }
}

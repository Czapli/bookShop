package org.czaplinski.bookshop.catalog.application.port;

import org.czaplinski.bookshop.catalog.domain.Author;

import java.util.List;

public interface AuthorsUseCase {
    List<Author> findAll();
}

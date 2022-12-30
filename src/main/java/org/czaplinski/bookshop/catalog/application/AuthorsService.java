package org.czaplinski.bookshop.catalog.application;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.catalog.application.port.AuthorsUseCase;
import org.czaplinski.bookshop.catalog.db.AuthorsJpaRepository;
import org.czaplinski.bookshop.catalog.domain.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorsService implements AuthorsUseCase {
    private final AuthorsJpaRepository repository;

    @Override
    public List<Author> findAll() {
        return repository.findAll();
    }
}

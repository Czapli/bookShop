package org.czaplinski.bookshop.catalog.db;

import org.czaplinski.bookshop.catalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorsJpaRepository extends JpaRepository<Author, Long> {
Optional<Author> findByNameIgnoreCase(String name);
}

package org.czaplinski.bookshop.catalog.db;

import org.czaplinski.bookshop.catalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsJpaRepository extends JpaRepository<Author, Long> {

}

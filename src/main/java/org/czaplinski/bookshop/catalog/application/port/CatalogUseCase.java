package org.czaplinski.bookshop.catalog.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.czaplinski.bookshop.catalog.domain.Book;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    Optional<Book> findOneByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    Optional<Book> findById(Long id);

    List<Book> findByTitleAndAuthor(String title, String Author);

    Book addBook(CreateBookCommand command);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void removeById(Long id);

    void updateBookCover(UpdateBookCoverCommand command);

    void removeBookCover(Long id);

    record UpdateBookCoverCommand(Long id, byte[] file, String contentType, String fileName) {
    }


        record CreateBookCommand(String title, Set<Long> authors, Integer year, BigDecimal price, Long available) {
    }


    @Builder

    record UpdateBookCommand(Long id, String title, Set<Long> authors, Integer year, BigDecimal price) {
    }


    record UpdateBookResponse(boolean success, List<String> errors) {
        public static UpdateBookResponse SUCCESS = new UpdateBookResponse(true, Collections.emptyList());
    }
}

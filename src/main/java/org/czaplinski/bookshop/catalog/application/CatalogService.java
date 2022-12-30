package org.czaplinski.bookshop.catalog.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public List<Book> findByTitle(String title) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getTitle().startsWith(title))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String author) {
        return catalogRepository.findAll().stream()
                .filter(book -> book.getAuthor().startsWith(author))
                .collect(Collectors.toList());
    }
}

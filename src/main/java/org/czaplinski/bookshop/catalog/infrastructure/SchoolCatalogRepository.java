package org.czaplinski.bookshop.catalog.infrastructure;

import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.catalog.domain.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryCatalogRepositoryImpl implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public MemoryCatalogRepositoryImpl() {
        storage.put(1L, new Book(1L, "Pan Tadeusz", "Adam Mickiewicz", 1834));
        storage.put(2L, new Book(2L, "Ogniem i Mieczem", "Henryk Sienkiewicz", 1884));
        storage.put(3L, new Book(3L, "Chlopi", "Wladyslaw Reymont", 1904));
        storage.put(4L, new Book(4L, "Pan Wlodyjowski", "Henryk Sienkiewicz", 1887));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}

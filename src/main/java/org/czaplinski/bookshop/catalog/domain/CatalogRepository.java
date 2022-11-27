package org.czaplinski.bookshop;

import org.czaplinski.bookshop.catalog.domain.Book;

import java.util.List;

public interface CatalogRepository {
    List<Book> listAll();
}

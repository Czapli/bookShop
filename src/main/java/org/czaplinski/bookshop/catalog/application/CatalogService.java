package org.czaplinski.bookshop.catalog.application;

import lombok.AllArgsConstructor;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase;
import org.czaplinski.bookshop.catalog.db.AuthorsJpaRepository;
import org.czaplinski.bookshop.catalog.db.BookJpaRepository;
import org.czaplinski.bookshop.catalog.domain.Author;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.czaplinski.bookshop.uploads.application.ports.UploadUseCase;
import org.czaplinski.bookshop.uploads.domain.Upload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.czaplinski.bookshop.uploads.application.ports.UploadUseCase.SaveUploadCommand;

@Service
@AllArgsConstructor
class CatalogService implements CatalogUseCase {
    private final BookJpaRepository bookRepository;
    private final AuthorsJpaRepository authorsRepository;
    private final UploadUseCase upload;

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleStartingWithIgnoreCase(title);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthors(author);
    }

    @Override
    public Optional<Book> findOneByTitle(String title) {
        return bookRepository.findFirstByTitleStartingWithIgnoreCase(title);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAllEager();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthors(title, author);

    }

    @Override
    @Transactional
    public Book addBook(CreateBookCommand command) {
        Book book = toBook(command);
        return bookRepository.save(book);
    }

    private Book toBook(CreateBookCommand command) {
        Book book = new Book(command.title(), command.year(), command.price(), command.available());
        Set<Author> authors = fetchAuthorByIds(command.authors());
        updateBook(book, authors);
        return book;
    }

    private void updateBook(Book book, Set<Author> authors) {
        book.removeAuthors();
        authors.forEach(book::addAuthor);
    }


    private Set<Author> fetchAuthorByIds(Set<Long> authors) {
        return authors
                .stream()
                .map(id -> authorsRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Enable find author with ID: " + id)))
                .collect(Collectors.toSet());
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookCommand command) {
        return bookRepository.findById(command.id())
                .map(book -> {
                    Book updatedBook = updateFields(command, book);
                    bookRepository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with ID: " + command.id())));
    }

    private Book updateFields(UpdateBookCommand command, Book book) {
        if (command.title() != null) {
            book.setTitle(command.title());
        }
        if (command.authors() != null && !command.authors().isEmpty()) {
            updateBook(book, fetchAuthorByIds(command.authors()));
        }
        if (command.year() != null) {
            book.setYear(command.year());
        }
        if (command.price() != null) {
            book.setPrice(command.price());
        }
        return book;
    }

    @Override
    public void removeById(Long id) {
        bookRepository.deleteById(id);

    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        int length = command.file().length;
        bookRepository.findById(command.id())
                .ifPresent(book -> {
                    Upload saveUpload = upload.save(new SaveUploadCommand(
                            command.fileName(),
                            command.file(),
                            command.contentType()));
                    book.setCoverId(saveUpload.getId());
                    bookRepository.save(book);
                });
    }

    @Override
    public void removeBookCover(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            if (book.getCoverId() != null) {
                upload.removeById(book.getCoverId());
                book.setCoverId(null);
                bookRepository.save(book);
            }
        });
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }


}

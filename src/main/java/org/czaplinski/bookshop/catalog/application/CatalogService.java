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
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthors(title, author);

    }

    @Override
    public Book addBook(CreateBookCommand command) {
        Book book = toBook(command);
        return bookRepository.save(book);
    }

    private Book toBook(CreateBookCommand command) {
        Book book = new Book(command.getTitle(), command.getYear(), command.getPrice());
        Set<Author> authors = fetchAuthorByIds(command.getAuthors());
        book.setAuthors(authors);
        return book;
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
        return bookRepository.findById(command.getId())
                .map(book -> {
                    Book updatedBook = updateFields(command, book);
                    bookRepository.save(updatedBook);
                    return UpdateBookResponse.SUCCESS;
                })
                .orElseGet(() -> new UpdateBookResponse(false, Arrays.asList("Book not found with ID: " + command.getId())));
    }

    private Book updateFields(UpdateBookCommand command, Book book) {
        if (command.getTitle() != null) {
            book.setTitle(command.getTitle());
        }
        if (command.getAuthors() != null && !command.getAuthors().isEmpty()) {
            book.setAuthors(fetchAuthorByIds(command.getAuthors()));
        }
        if (command.getYear() != null) {
            book.setYear(command.getYear());
        }
        if (command.getPrice() != null) {
            book.setPrice(command.getPrice());
        }
        return book;
    }

    @Override
    public void removeById(Long id) {
        bookRepository.deleteById(id);

    }

    @Override
    public void updateBookCover(UpdateBookCoverCommand command) {
        int length = command.getFile().length;
        bookRepository.findById(command.getId())
                .ifPresent(book -> {
                    Upload saveUpload = upload.save(new SaveUploadCommand(
                            command.getFileName(),
                            command.getFile(),
                            command.getContentType()));
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

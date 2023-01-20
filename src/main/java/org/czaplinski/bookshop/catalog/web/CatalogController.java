package org.czaplinski.bookshop.catalog.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.czaplinski.bookshop.CreatedURI;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase.CreateBookCommand;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import org.czaplinski.bookshop.catalog.application.port.CatalogUseCase.UpdateBookResponse;
import org.czaplinski.bookshop.catalog.domain.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/catalog")
@RestController
@AllArgsConstructor
public class CatalogController {
    private final CatalogUseCase catalog;

    @GetMapping
    public ResponseEntity<List<Book>> getAll(
            @RequestParam
            Optional<String> title,
            Optional<String> author) {
        if (title.isPresent() && author.isPresent()) {
            return ResponseEntity.ok(catalog.findByTitleAndAuthor(title.get(), author.get()));
        } else if (title.isPresent()) {
            return ResponseEntity.ok(catalog.findByTitle(title.get()));
        } else if (author.isPresent()) {
            return ResponseEntity.ok(catalog.findByAuthor(author.get()));
        }
        return ResponseEntity.ok(catalog.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return catalog
                .findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@Valid @RequestBody RestBookCommand command) {
        Book book = catalog.addBook(command.toCreateBookCommand());
        URI uri = createdBookUri(book);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        catalog.removeById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBook(@PathVariable Long id, @RequestBody RestBookCommand command) {
        UpdateBookResponse response = catalog.updateBook(command.toUpdateCommand(id));
        if (!response.success()) {
            String message = String.join("' ", response.errors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PutMapping(value = "/{id}/cover", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Got file" + file.getOriginalFilename());

        catalog.updateBookCover(new CatalogUseCase.UpdateBookCoverCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()

        ));
    }

    @DeleteMapping("/{id}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookCover(@PathVariable Long id) {
        catalog.removeBookCover(id);
    }

    private static URI createdBookUri(Book book) {
        return new CreatedURI("/" + book.getId().toString()).uri();
    }

    @Data
    private static class RestBookCommand {
        @NotBlank(message = "Pleas provide title")
        private String title;
        @NotEmpty(message = "Pleas provide author")
        private Set<Long> authors;
        @NotNull(message = "Pleas provide title")
        private Integer year;
        @PositiveOrZero
        private Long available;
        @NotNull(message = "Pleas provide price")
        @DecimalMin(value = "0.00", message = "Price must be grater by zero")
        private BigDecimal price;

        CreateBookCommand toCreateBookCommand() {
            return new CreateBookCommand(title, authors, year, price, available);
        }

        UpdateBookCommand toUpdateCommand(Long id) {
            return new UpdateBookCommand(id, title, authors, year, price);
        }
    }
}

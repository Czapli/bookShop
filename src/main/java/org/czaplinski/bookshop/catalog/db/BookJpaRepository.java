package org.czaplinski.bookshop.catalog.db;

import org.czaplinski.bookshop.catalog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthors_firstNameContainsIgnoreCaseOrAuthors_lastNameContainsIgnoreCase(String firstName, String lastName);

    Optional<Book> findFirstByTitleStartingWithIgnoreCase(String title);

    List<Book> findByTitleStartingWithIgnoreCase(String title);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllEager();

    @Query(
            "SELECT b " +
                    " FROM Book b JOIN b.authors a " +
                    " WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%'))" +
                    " OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))"
    )
    List<Book> findByAuthors(@Param("name") String name);

    @Query(
            "SELECT b " +
                    " FROM Book b JOIN b.authors a " +
                    " WHERE LOWER(b.title) LIKE LOWER(CONCAT(:title, '%'))" +
                    " AND (LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%'))" +
                    " OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%')))"
    )
    List<Book> findByTitleAndAuthors(@Param("title") String title, @Param("name") String name);

}
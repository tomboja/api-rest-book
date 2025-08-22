package cs599.edu.miu.boja.qr.api.books.repository;

import cs599.edu.miu.boja.qr.api.books.domain.entity.BookEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */

@ApplicationScoped
@Slf4j
public class BooksRepository implements PanacheRepository<BookEntity> {

    public List<BookEntity> saveAllBooks(List<BookEntity> bookEntities) {
        BookEntity.persist(bookEntities);
        log.info("Books saved successfully: " + bookEntities);
        return bookEntities;
    }

    public List<BookEntity> findAllBooks() {
        return BookEntity.listAll();
    }

    public BookEntity saveBook(BookEntity bookEntity) {
        BookEntity.persist(bookEntity);
        log.info("Book saved successfully: " + bookEntity);
        return bookEntity;
    }

    public Optional<BookEntity> findBookById(Long id) {
        log.info("Fetch Book with Id: " + id);
        BookEntity bookEntity = BookEntity.findById(id);
        return Optional.ofNullable(bookEntity);
    }

    public Optional<BookEntity> findBookByTitleAuthorYearOfPublication(String title, String author, String yearOfPublication) {
        log.info("Finding book by title: " + title + ", author: " + author + ", year: " + yearOfPublication);
        return BookEntity
                .find("title = ?1 and author = ?2 and publicationYear = ?3", title, author, yearOfPublication)
                .firstResultOptional();
    }

    public Optional<BookEntity> findBookByTitle(String title) {
        log.info("Finding book by title: " + title);
        return BookEntity.find("title", title).firstResultOptional();
    }

    public List<BookEntity> findBooksByTitlePartial(String partialTitle) {
        log.info("Finding books with title containing: " + partialTitle);
        return BookEntity.list("lower(title) like ?1", "%" + partialTitle.toLowerCase() + "%");
    }

    public BookEntity updateBook(BookEntity bookEntity) {
        BookEntity merged = getEntityManager().merge(bookEntity);
        log.info("Book updated: {}", merged);
        return merged;
    }

    public boolean deleteBook(Long id) {
        log.info("Deleting book with ID: " + id);
        BookEntity bookEntity = BookEntity.findById(id);
        if (bookEntity == null) {
            log.warn("Book with ID " + id + " not found for deletion.");
            return false;
        }
        bookEntity.delete();
        return true;
    }

    public Optional<BookEntity> findBookByISBN(String isbn) {
        log.info("Finding book by ISBN: " + isbn);
        return BookEntity.find("isbn", isbn).firstResultOptional();
    }
}

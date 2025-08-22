package cs599.edu.miu.boja.qr.api.books.service;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.domain.entity.BookEntity;
import cs599.edu.miu.boja.qr.api.books.exception.DuplicateResourceException;
import cs599.edu.miu.boja.qr.api.books.exception.ResourceNotFoundException;
import cs599.edu.miu.boja.qr.api.books.mapper.BookMapper;
import cs599.edu.miu.boja.qr.api.books.repository.BooksRepository;
import cs599.edu.miu.boja.qr.api.books.util.BookEntityDataSeedUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */

@ApplicationScoped
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    // This class will contain methods to handle book-related operations
    // such as adding, updating, deleting, and retrieving books.

    @Inject
    BooksRepository bookRepository;

    public Book findBookById(Long id) {
        return bookRepository.findBookById(id)
                .map(BookMapper::mapBookEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    // Example method to update a book
    @Transactional
    public Book updateBook(Long id, Book book) {
        // find the book by id
        BookEntity bookEntity = bookRepository.findBookById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Update the existing book entity with new values
        if (StringUtils.isNotBlank(book.getTitle())) {
            bookEntity.setTitle(book.getTitle());
        }
        if (StringUtils.isNotBlank(book.getAuthor())) {
            bookEntity.setAuthor(book.getAuthor());
        }
        if (StringUtils.isNotBlank(book.getPublicationYear())) {
            bookEntity.setPublicationYear(book.getPublicationYear());
        }
        if (StringUtils.isNotBlank(book.getGenre())) {
            bookEntity.setGenre(book.getGenre());
        }
        if (StringUtils.isNotBlank(book.getDescription())) {
            bookEntity.setDescription(book.getDescription());
        }

        // Save the updated book
        bookRepository.saveBook(bookEntity);
        log.info("Book updated successfully: {}", bookEntity);
        return BookMapper.mapBookEntity(bookEntity);
    }

    @Transactional
    public boolean deleteBook(Long id) {
        // This method should delete a book by its ID
        Book book = bookRepository.findBookById(id)
                .map(BookMapper::mapBookEntity)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new ResourceNotFoundException("Book not found with id: " + id);
                });

       try {
           boolean deleted = bookRepository.deleteBook(id);
           if (deleted) {
               log.info("Book deleted successfully: {}", book);
           } else {
               log.error("Book not found for deletion: {}", id);
           }
           return deleted;
       } catch (Exception exception) {
           log.error("Error deleting book: ", exception);
           return false;
       }
    }

    // Example method to retrieve all books
    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks()
                .stream().map(BookMapper::mapBookEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Book addBook(@Valid Book book) {

        // Check for duplicate by title, author, and publication year
        if (bookRepository.findBookByTitleAuthorYearOfPublication(
                book.getTitle(), book.getAuthor(), book.getPublicationYear()).isPresent()) {
            log.error("Duplicate book detected: title={}, author={}, year={}",
                    book.getTitle(), book.getAuthor(), book.getPublicationYear());
            throw new DuplicateResourceException("Book with same title, author, and publication year already exists");
        }

        BookEntity bookEntity = BookMapper.mapBook(book);
        BookEntity persistedBookEntity = bookRepository.saveBook(bookEntity);
        if (persistedBookEntity == null) {
            log.error("Failed to save book: {}", book);
            throw new RuntimeException("Failed to save book");
        }
        log.info("Book added successfully: {}", persistedBookEntity);
        return BookMapper.mapBookEntity(persistedBookEntity);
    }

    @Transactional
    public List<Book> seedBooks() {
        List<BookEntity> bookEntities = BookEntityDataSeedUtil.seedBookEntities();
        List<BookEntity> bookEntity = bookRepository.saveAllBooks(bookEntities);
        if (bookEntity == null || bookEntity.isEmpty()) {
            log.error("No books were saved during seeding.");
            throw new RuntimeException("No books were saved during seeding.");
        }
        log.info("Books seeded successfully: {}", bookEntity);


        return bookEntities.stream()
                .map(BookMapper::mapBookEntity)
                .toList();
    }
}

package cs599.edu.miu.boja.qr.api.books.service;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.exception.BookConstraintViolationException;
import cs599.edu.miu.boja.qr.api.books.exception.ResourceNotFoundException;
import cs599.edu.miu.boja.qr.api.books.repository.BooksRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

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

    public List<Book> saveSampleBooks() {
        return bookRepository.saveAllBooks();
    }

   public Book findBookById(Long id) {
       Book book = bookRepository.findBookById(id);
       if (book == null) {
           throw new ResourceNotFoundException("Book not found with id: " + id);
       }
       return book;
   }

    // Example method to update a book
    @Transactional
    public Book updateBook(Long id, Book book) {
        // find the book by id
        Book existingBook = bookRepository.findBookById(id);
        if (existingBook == null) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        // Update the existing book with new values
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublicationYear(book.getPublicationYear());
        existingBook.setGenre(book.getGenre());
        existingBook.setLanguage(book.getLanguage());
        existingBook.setDescription(book.getDescription());
        // Save the updated book
        bookRepository.saveBook(existingBook);
        log.info("Book updated successfully: {}", existingBook);
        return existingBook;
    }

    @Transactional
    public boolean deleteBook(Long id) {
        // This method should delete a book by its ID
        Book book = bookRepository.findBookById(id);
        if (book == null) {
            log.error("Book not found with id: {}", id);
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        try {
            bookRepository.deleteBook(book);
            log.info("Book deleted successfully: {}", book);
            return true;
        } catch (Exception e) {
            log.error("Error deleting book: {}", e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    // Example method to retrieve all books
    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

   @Transactional
   public Book addBook(Book book) {
       if (book == null) {
           log.error("Book cannot be null");
           throw new IllegalArgumentException("Book cannot be null");
       }
       try {
           bookRepository.saveBook(book);
           log.info("Book saved successfully: {}", book);
           return book;
       } catch (ConstraintViolationException e) {
           throw new BookConstraintViolationException("Constraint violation: " + e.getMessage(), e);
       } catch (Exception e) {
           log.error("Error saving book: {}", e.getMessage());
           log.error(Arrays.toString(e.getStackTrace()));
           throw e; // Let the generic exception mapper handle it
       }
   }

}

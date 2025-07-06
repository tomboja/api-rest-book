package cs599.edu.miu.boja.qr.api.books.repository;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.persist;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */

@ApplicationScoped
public class BooksRepository {

    private static final Logger log = LoggerFactory.getLogger(BooksRepository.class);

    // Let's Create collection of books to insert to db when application starts
    public List<Book> getSampleBooks() {

        return List.of(
                    new Book("978-3-16-148410-0", "Effective Java", "Joshua Bloch", "Addison-Wesley", 2018, "Programming", "English", "A comprehensive guide to programming in Java."),
                    new Book("978-0-13-468609-7", "Clean Code", "Robert C. Martin", "Prentice Hall", 2008, "Programming", "English", "A handbook of agile software craftsmanship."),
                    new Book("978-0-201-53082-7", "Design Patterns: Elements of Reusable Object-Oriented Software", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", "Addison-Wesley", 1994, "Software Design", "English", "A classic book on software design patterns."),
                    new Book("978-1-4919-1882-6", "Learning Python", "Mark Lutz", "O'Reilly Media", 2013, "Programming", "English", "An in-depth introduction to the Python programming language.")
            );
    }

    @Transactional
    public List<Book> saveAllBooks() {
        List<Book> books = getSampleBooks();
        try {
            persist(books);
            log.info("Sample books saved successfully.");
            log.info(books.toString());
            return books;
        } catch (Exception e) {
            // Log or handle the error as needed
            log.error(e.getMessage());
            return List.of(); // or rethrow, or return partial results if needed
        }
    }

    public List<Book> findAllBooks() {
        return Book.listAll();
    }

    public Book saveBook(Book book) {
        persist(book);
        log.info("Book saved successfully: " + book);
        return book;
    }


    public Book findBookById(Long id) {
        if (id == null) {
            log.error("Book ID cannot be null");
            return null;
        }
        return Book.findById(id);
    }

    public void deleteBook(Book book) {
        book.delete();
    }
}

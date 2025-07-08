package cs599.edu.miu.boja.qr.api.books;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

@Path("/api/books")
public class BooksApplication {
    private static final Logger log = LoggerFactory.getLogger(BooksApplication.class);
    @Inject
    BookService bookService;
    @Inject
    DataSource dataSource;

    @POST
    @Path("/save")
    public List<Book> saveSampleBooks() {
        return bookService.saveSampleBooks();
    }

    @GET
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @POST
    public Book addBook(Book book) {
        log.info("Adding book: " + book);
        return bookService.addBook(book);
    }

    @GET
    @Path("/{id}")
    public Book findBookById(@PathParam("id") Long id) {
        log.info("Finding book by ID: " + id);
        return bookService.findBookById(id);
    }

    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") Long id, Book book) {
        log.info("Updating book with ID: " + id);
        return bookService.updateBook(id, book);
    }

    @DELETE
    @Path("/{id}")
    public void deleteBook(@PathParam("id") Long id) {
        log.info("Deleting book with ID: " + id);
        bookService.deleteBook(id);
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public String health() {
        try (var connection = dataSource.getConnection()) {
            if (connection != null) {
                return "Database connection is successful!";
            } else {
                return "Failed to connect to the database.";
            }
        } catch (Exception e) {
            return "Error connecting to the database: " + e.getMessage();
        }
    }
}

package cs599.edu.miu.boja.qr.api.books;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/books")
public class BooksApplication {
    private static final Logger log = LoggerFactory.getLogger(BooksApplication.class);
    @Inject
    BookService bookService;

    @GET
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

}

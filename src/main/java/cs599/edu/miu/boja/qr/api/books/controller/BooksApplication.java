package cs599.edu.miu.boja.qr.api.books.controller;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.net.URI;
import java.util.List;

@Path("/api/books")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class BooksApplication {

    @Inject
    BookService bookService;
    @Inject
    DataSource dataSource;

    @POST
    @Path("/save")
    public Response saveSampleBooks() {
        List<Book> books = bookService.seedBooks();
        return Response.status(Response.Status.CREATED)
                .entity(books)
                .build();
    }

    @GET
    public Response getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return Response.status(Response.Status.OK)
                .entity(books)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    public Response addBook(Book book) {
        Book savedBook = bookService.addBook(book);
        URI location = URI.create("/api/books/" + savedBook.getId());
        return Response.created(location)
                .entity(savedBook)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build();
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
    @Produces(MediaType.TEXT_PLAIN)
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

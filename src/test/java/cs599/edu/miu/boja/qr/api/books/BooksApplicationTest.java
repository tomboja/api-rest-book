package cs599.edu.miu.boja.qr.api.books;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class BooksApplicationTest {
    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/api/books")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", isA(java.util.List.class));
    }

    @Test
    void testAddBook() {
        Book book = new Book("999-1-23-456789-0", "JUnit in Action", "Sam Brannen", "Manning", 2020, "Testing", "English", "JUnit 5 guide");
        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when().post("/api/books")
                .then()
                .statusCode(200)
                .body("isbn", equalTo("999-1-23-456789-0"))
                .body("title", equalTo("JUnit in Action"));
    }

    @Test
    void testFindBookById_NotFound() {
        given()
                .when().get("/api/books/999999")
                .then()
                .statusCode(404)
                .body("message", containsString("Book not found"));
    }

    @Test
    void testSaveSampleBooks() {
        given()
                .when().post("/api/books/save")
                .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        Book book = new Book("111-1-11-111111-1", "Nonexistent", "Nobody", "Nowhere", 2022, "None", "English", "Does not exist");
        given()
                .contentType(ContentType.JSON)
                .body(book)
                .when().put("/api/books/999999")
                .then()
                .statusCode(404)
                .body("message", containsString("Book not found"));
    }

    @Test
    void testDeleteBook_NotFound() {
        given()
                .when().delete("/api/books/999999")
                .then()
                .statusCode(404)
                .body("message", containsString("Book not found"));
    }

}
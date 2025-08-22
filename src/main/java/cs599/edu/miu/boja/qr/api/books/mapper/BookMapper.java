package cs599.edu.miu.boja.qr.api.books.mapper;

import cs599.edu.miu.boja.qr.api.books.domain.Book;
import cs599.edu.miu.boja.qr.api.books.domain.entity.BookEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/16/25
 */

@Slf4j
public final class BookMapper {

    private BookMapper() {}

    public static BookEntity mapBook(Book book) {
        if (book == null) {
            log.warn("Attempted to map a null Book object.");
            return null;
        }

        return BookEntity.builder()
                .genre(book.getGenre())
                .author(book.getAuthor())
                .description(book.getDescription())
                .isbn13(book.getIsbn())
                .language(book.getLanguage())
                .publicationYear(book.getPublicationYear())
                .publisher(book.getPublisher())
                .title(book.getTitle())
                .build();
    }

    public static Book mapBookEntity(BookEntity bookEntity) {
        if (bookEntity == null) {
            log.warn("Attempted to map a null BookEntity object.");
            return null;
        }

        return Book.builder()
                .id(bookEntity.getId())
                .genre(bookEntity.getGenre())
                .author(bookEntity.getAuthor())
                .description(bookEntity.getDescription())
                .isbn(bookEntity.getIsbn13())
                .language(bookEntity.getLanguage())
                .publicationYear(bookEntity.getPublicationYear())
                .publisher(bookEntity.getPublisher())
                .title(bookEntity.getTitle())
                .build();
    }
}

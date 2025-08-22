package cs599.edu.miu.boja.qr.api.books.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
@Getter
@Setter
public class Book {
    @Builder.Default
    private Long id = null;
    @NotBlank(message = "Book ISBN is required")
    @Pattern(
        regexp = "^97[89]-\\d-\\d{2}-\\d{6}-\\d$",
        message = "Book ISBN must be a valid 13 digit ISBN number"
    )
    private String isbn;
    @NotBlank(message = "Book title is required")
    private String title;
    @NotBlank(message = "Book author is required")
    private String author;
    @NotBlank(message = "Book publisher is required")
    private String publisher;
    @NotBlank(message = "Book publication year is required")
    @Pattern(
            regexp = "^\\d{4}$",
            message = "Publication year must be a 4-digit number"
    )
    private String publicationYear;
    @NotBlank(message = "Book genre is required")
    private String genre;
    @NotBlank(message = "Book language is required")
    private String language;
    @NotBlank(message = "Book description is required")
    private String description;
}

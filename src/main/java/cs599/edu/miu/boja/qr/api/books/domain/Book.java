package cs599.edu.miu.boja.qr.api.books.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 7/4/25
 */

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "books")
public class Book extends PanacheEntity {
    @Column(unique = true)
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private String genre;
    private String language;
    private String description;
}

package cs599.edu.miu.boja.qr.api.books.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @ProjectName: api-rest-book
 * @Author: Temesgen D.
 * @Date: 8/16/25
 */


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Builder
@Table(name = "books")
public class BookEntity extends PanacheEntity {
    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn13;
    private String title;
    private String author;
    private String publisher;
    private String publicationYear;
    private String genre;
    private String language;
    private String description;

    public Long getId() {
        return id;
    }
}

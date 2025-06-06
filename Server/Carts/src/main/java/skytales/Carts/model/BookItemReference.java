package skytales.Carts.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BookItemReference {

    @Id
    private UUID id;

    private String title;

    private String author;

    private String genre;

    @Column(name = "cover_image_url", length = 1000)
    private String coverImageUrl;

    @Column(nullable = false)
    private Integer year;

    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookItemReference that = (BookItemReference) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

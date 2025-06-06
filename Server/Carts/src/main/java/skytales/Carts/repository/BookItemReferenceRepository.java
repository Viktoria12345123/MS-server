package skytales.Carts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skytales.Carts.model.BookItemReference;


import java.util.UUID;

@Repository
public interface BookItemReferenceRepository extends JpaRepository<BookItemReference, UUID> {
}

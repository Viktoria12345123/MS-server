package skytales.Carts.service;

import org.springframework.stereotype.Service;
import skytales.Carts.web.dto.BookRequest;
import skytales.Carts.model.BookItemReference;
import skytales.Carts.repository.BookItemReferenceRepository;
import skytales.Carts.util.state_engine.dto.BookMessage;


@Service
public class BookReferenceService {

    private final BookItemReferenceRepository bookItemReferenceRepository;

    public BookReferenceService(BookItemReferenceRepository bookItemReferenceRepository) {
        this.bookItemReferenceRepository = bookItemReferenceRepository;
    }

    public void addBookToState( BookMessage bookRequest) {

        BookItemReference book = BookItemReference.builder()
                .author(bookRequest.author())
                .title(bookRequest.title())
                .year(bookRequest.year())
                .price(bookRequest.price())
                .genre(bookRequest.genre())
                .quantity(bookRequest.quantity())
                .coverImageUrl(bookRequest.coverImageUrl())
                .bookId(bookRequest.id())
                .build();


        bookItemReferenceRepository.save(book);
    }

    public void removeBookFromState(BookRequest BookRequest) {

        BookItemReference book = bookItemReferenceRepository.findById(BookRequest.id()).orElse(null);

        if (book != null) {
            bookItemReferenceRepository.delete(book);
        }

    }

    public void updateBookStock(BookMessage bookRequest) {

    }
}

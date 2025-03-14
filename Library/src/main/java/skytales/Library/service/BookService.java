package skytales.Library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import skytales.Library.util.state_engine.UpdateProducer;
import skytales.Library.util.state_engine.model.UpdateType;
import skytales.Library.web.dto.BookData;
import skytales.Library.util.elasticsearch.service.ElasticSearchService;
import skytales.Library.model.Book;
import skytales.Library.repository.BookRepository;


import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ElasticSearchService elasticSearchService;
    private final UpdateProducer updateProducer;

    @Autowired
    public BookService(BookRepository bookRepository, ElasticSearchService elasticSearchService, UpdateProducer updateProducer) {
        this.bookRepository = bookRepository;
        this.elasticSearchService = elasticSearchService;
        this.updateProducer = updateProducer;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(UUID bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public List<Book> getNewestBooks(int year) {
        return bookRepository.findBooksByYearAfter(year)
                .orElseThrow(() -> new NoSuchElementException("No books found after the year " + year));
    }


    public Book createBook(BookData data) {

        BigDecimal price = new BigDecimal(data.price());

        Book book = Book.builder()
                .title(data.title())
                .author(data.author())
                .coverImageUrl(data.coverImageUrl())
                .bannerImageUrl(data.bannerImageUrl())
                .price(price)
                .description(data.description())
                .year(Integer.parseInt(data.year()))
                .genre(data.genre())
                .quantity(Integer.parseInt(data.quantity()))
                .build();

        bookRepository.save(book);
        elasticSearchService.addBookToElasticsearch(book);
        updateProducer.sendBookUpdate(UpdateType.NEW_BOOK, book);

        return book;
    }
}

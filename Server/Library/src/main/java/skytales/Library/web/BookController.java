package skytales.Library.web;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import skytales.Library.model.Book;
import skytales.Library.service.BookService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ElasticsearchClient elasticsearchClient;

    public BookController(BookService bookService, ElasticsearchClient elasticsearchClient) {
        this.bookService = bookService;
        this.elasticsearchClient = elasticsearchClient;
    }

    @GetMapping()
    public ResponseEntity<List<?>> getBooks(@RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch, HttpServletResponse response) {

        List<Book> books = bookService.getAllBooks();
        String generatedETag = generateETagForBooks(books);

        if (ifNoneMatch != null && ifNoneMatch.equals(generatedETag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        response.setHeader(HttpHeaders.ETAG, generatedETag);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
                .body(books);
    }

//    @PostMapping("/create")
//    public ResponseEntity<Book> createBook(
//            @Valid @ModelAttribute BookData bookData,
//            BindingResult bindingResult,
//            @RequestParam("bannerImage") MultipartFile bannerImage,
//            @RequestParam("coverImage") MultipartFile coverImage)  throws IOException {
//
//        if (bindingResult.hasErrors()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//
//        Book book = bookService.createBook(bookData, bannerImage, coverImage);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
//                .body(book);
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBooks(@RequestBody ArrayList<String> bookIds) {

        if (bookIds == null || bookIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Book ID list cannot be empty.");
        }

        bookService.deleteBooks(bookIds);

        return ResponseEntity.ok("Books deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {

        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("general-search")
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(query)
                                    .fields("title^3", "author^2", "description")
                                    .fuzziness("AUTO")
                            ))
            );

            SearchResponse<Book> searchResponse = elasticsearchClient.search(searchRequest, Book.class);

            List<Book> books = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(books, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable String bookId) {

        Book book = bookService.getBookById(UUID.fromString(bookId));

        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/newest")
    public ResponseEntity<?> getNewest(@RequestParam int year) {
        List<Book> books = bookService.getNewestBooks(year);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    private String generateETagForBooks(List<Book> books) {
        return Integer.toHexString(books.hashCode());
    }
}

package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.controller.BookController;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.Book;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.CreateBookData;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.DetailBookData;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookService service;
    @Mock
    private BookRepository repository;
    private Book book;
    private DetailBookData bookData;
    private CreateBookData createBookData;


    @BeforeEach
    void setUp() {
        var date = Date.from(Instant.now());
        this.book = new Book("BBB", date , 50.00, "XXX");
        book.setId(1L);
        book.add(linkTo(methodOn(BookController.class).search(book.getId())).withSelfRel());


        this.bookData = new DetailBookData(1L, "CCC", Date.from(Instant.now()), 50.00, "YYY",   book.getLinks());
        this.createBookData = new CreateBookData("DDD", Date.from(Instant.now()), 50.00, "ZZZ",  book.getLinks());
    }

    @Test
    void testSearchBook(){

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.searchBook(1L);

        verify(repository).findById(1L);

        var link = linkTo(methodOn(BookController.class)
                .search(book.getId())).withSelfRel();

        assertNotNull(result);
        assertNotNull(result.id());
        assertNotNull(result.author());
        assertNotNull(result.launchDate());
        assertNotNull(result.price());
        assertNotNull(result.title());
        assertNotNull(result.link());
        assertTrue(result.link().contains(link));
        assertTrue(result.toString().contains("link=</api/books/1>;rel=\"self\""));
    }

    @Test
    void testCreated(){

        Book entity = new Book(createBookData);
        entity.setId(1L);


        entity.add(Link.of("/api/books/" + book.getId()));

        when(repository.save(any(Book.class))).thenReturn(entity);

        var result = service.created(createBookData);

        verify(repository).save(any(Book.class));

        assertNotNull(result);
        assertNotNull(result.link());
        assertEquals("DDD", result.author());
        assertEquals(createBookData.launchDate(), result.launchDate());
        assertEquals(50.00, result.price());
        assertEquals("ZZZ", result.title());

        assertTrue(result.toString().contains("link=</api/books/1>;rel=\"self\""));

    }

    @Test
    @DisplayName("Should throw RequiredObjectIsNullException message if the object is null")
    void testCreatedIsNull(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () ->{
            service.created(null);
        });

        String expected = "It's not allowed to persist a null object";
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }
    @Test
    void testUpdatePerson(){
        //prepare DetailBookData
//        this.bookData = new DetailBookData(book);

        //Mock the repository
        when(repository.getReferenceById(any())).thenReturn(book);
        when(repository.save(book)).thenAnswer(invocation -> invocation.getArgument(0));

        //Call the service method
        var result = service.updateBook(bookData);

        //Verify the repository methods were called
        verify(repository).getReferenceById(bookData.id());
        verify(repository).save(book);


        //Verify that the book details were updated
        assertEquals(1L, book.getId());
        assertEquals("CCC", book.getAuthor());
        assertEquals(bookData.launchDate(), book.getLaunchDate());
        assertEquals(50.00, book.getPrice());
        assertEquals("YYY", book.getTitle());


        // Verify the returned data
        assertNotNull(result);
        assertNotNull(result.link());
        assertEquals(result.author(), book.getAuthor());
        assertEquals(result.launchDate(), book.getLaunchDate());
        assertEquals(result.price(), book.getPrice());
        assertEquals(result.title(), book.getTitle());

        assertTrue(result.toString().contains("link=</api/books/1>;rel=\"self\""));

    }

    @Test
    @DisplayName("Should throw RequiredObjectIsNullException message if the object is null")
    void testUpdateObjectIsNull(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () ->{
            service.updateBook(null);
        });

        String expected = "It's not allowed to persist a null object";
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }
    @Test
    void testFindAllBooks(){

        List<Book> books = new ArrayList<>();

        var date1 = Date.from(Instant.now());
        var book1 = new Book("DDD", date1, 50.00, "HHH");
        var book2 = new Book("FFF", date1, 50.00, "JJJ");

        book1.setId(1L);
        book2.setId(2L);
        books.add(book1);
        books.add(book2);

//        books.forEach(b -> b.add(Link.of("/api/books/" + b.getId())));


        when(repository.findAll()).thenReturn(books);

        var result = service.findAllBooks();

        assertNotNull(result);

        var resultBook1 = result.get(0);

        assertNotNull(resultBook1);
        assertNotNull(resultBook1.id());
        assertNotNull(resultBook1.link());

        System.out.println(resultBook1.toString());
        assertTrue(resultBook1.toString().contains("link=</api/books/1>;rel=\"self\""));
        assertEquals("DDD", resultBook1.author());
        assertEquals(book1.getLaunchDate(), resultBook1.launchDate());
        assertEquals(50.00, resultBook1.price());
        assertEquals("HHH", resultBook1.title());


        var resultBook2 = result.get(1);

        assertNotNull(resultBook2);
        assertNotNull(resultBook2.id());
        assertNotNull(resultBook2.link());

        assertTrue(resultBook2.toString().contains("link=</api/books/2>;rel=\"self\""));
        assertEquals("FFF", resultBook2.author());
        assertEquals(book2.getLaunchDate(), resultBook2.launchDate());
        assertEquals(50.00, resultBook2.price());
        assertEquals("JJJ", resultBook2.title());

    }


}
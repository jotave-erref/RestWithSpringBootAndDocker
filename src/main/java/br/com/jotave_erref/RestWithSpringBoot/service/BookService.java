package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.Controller.BookController;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.Book;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.CreateBookData;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.DetailBookData;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.ResourceNotFoundException;
import br.com.jotave_erref.RestWithSpringBoot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public CreateBookData created(CreateBookData data) {
        if(data == null)    throw new RequiredObjectIsNullException();
        Book book = new Book(data);
        var persistedBook = repository.save(book);
        book.add(linkTo(methodOn(BookController.class).search(persistedBook.getId())).withSelfRel());
        return new CreateBookData(persistedBook);
    }

    public List<DetailBookData> findAllBooks() {
        var books =  repository.findAll();

        books.stream().forEach(l -> l.add(linkTo(methodOn(BookController.class).search(l.getId())).withSelfRel()));

         return books.stream().map(b -> new DetailBookData(b.getId(), b.getAuthor(), b.getLaunchDate(), b.getPrice(), b.getTitle(), b.getLinks())).collect(Collectors.toList());

    }

    public DetailBookData searchBook(Long id) {
        var book = repository.findById(id).get();
        book.add(linkTo(methodOn(BookController.class).search(id)).withSelfRel());
        return new DetailBookData(book);

    }

    public DetailBookData updateBook(DetailBookData data) {
        if(data == null) throw new RequiredObjectIsNullException();
        var book = repository.getReferenceById(data.id());
        if(book == null) throw new ResourceNotFoundException("No records found for this ID");
        book.update(data);
        var updatePerson = repository.save(book);
        book.add(linkTo(methodOn(BookController.class).search(book.getId())).withSelfRel());
        return new DetailBookData(updatePerson);
    }

    public void deleteBook(Long id) {
        repository.deleteById(id);
    }
}

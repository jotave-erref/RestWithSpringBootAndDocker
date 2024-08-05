package br.com.jotave_erref.RestWithSpringBoot.repository;

import br.com.jotave_erref.RestWithSpringBoot.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookRepository extends JpaRepository<Book, Long> {
}

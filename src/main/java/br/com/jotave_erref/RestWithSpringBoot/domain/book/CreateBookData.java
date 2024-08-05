package br.com.jotave_erref.RestWithSpringBoot.domain.book;

import org.springframework.hateoas.Links;

import java.time.LocalDateTime;
import java.util.Date;

public record CreateBookData(String author, Date launchDate, Double price, String title, Links link) {
    public CreateBookData(Book data) {
        this(data.getAuthor(), data.getLaunchDate(), data.getPrice(), data.getTitle(), data.getLinks());
    }
}

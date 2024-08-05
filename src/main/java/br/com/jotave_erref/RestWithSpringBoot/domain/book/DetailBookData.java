package br.com.jotave_erref.RestWithSpringBoot.domain.book;

import org.springframework.hateoas.Links;

import java.time.LocalDateTime;
import java.util.Date;

public record DetailBookData(Long id, String author, Date launchDate, Double price, String title, Links link) {
    public DetailBookData(Book data) {
        this(data.getId(), data.getAuthor(), data.getLaunchDate(), data.getPrice(), data.getTitle(), data.getLinks());
    }
}

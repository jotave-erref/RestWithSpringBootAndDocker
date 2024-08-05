package br.com.jotave_erref.RestWithSpringBoot.domain.book;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book extends RepresentationModel<Book> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    @Column(name = "launch_date")
    @Temporal(TemporalType.DATE)
    private Date launchDate;
    private Double price;
    private String title;
    public Book(){}

    public Book(String author, Date launchDate, Double price, String text) {
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = text;
    }

    public Book(CreateBookData data) {
        this.author = data.author();
        this.launchDate = data.launchDate();
        this.price = data.price();
        this.title = data.title();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book books = (Book) o;
        return Double.compare(price, books.price) == 0 && Objects.equals(id, books.id) && Objects.equals(author, books.author) && Objects.equals(launchDate, books.launchDate) && Objects.equals(title, books.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launchDate, price, title);
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", launchDate=" + launchDate +
                ", price=" + price +
                ", text='" + title + '\'' +
                '}';
    }

    public void update(DetailBookData data) {
        if(data.author() != null){
            this.author = data.author();
        }
        if(data.launchDate() != null){
            this.launchDate = data.launchDate();
        }
        if(data.price() != null){
            this.price = data.price();
        }
        if(data.title() != null){
            this.title = data.title();
        }
    }
}


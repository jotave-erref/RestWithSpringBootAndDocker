package br.com.jotave_erref.RestWithSpringBoot.integrationtests.data;

import java.util.Date;

public class BookDataTest {
    private Long id;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    public BookDataTest(){
    }

    public BookDataTest(Long id,String author, Date launchDate, Double price, String title) {
        this.id = id;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookDataTest{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", launchDate=" + launchDate +
                ", price=" + price +
                ", title='" + title + '\'' +
                '}';
    }
}


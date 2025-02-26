package dev.folomkin.mockitospringbootdemo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>();

    public List<Book> findAll() {
        return books;
    }

    public Book findOne(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElseThrow();
    }

    @PostConstruct
    private void loadBooks() {
        Book one = new Book(1,
                "97 Things Every Java Programmer Should Know",
                "Kevlin Henney, Trisha Gee",
                "OReilly Media, Inc.",
                "May 2020",
                "9781491952696",
                "Java");
        Book two = new Book(2,
                "Spring Boot: Up and Running",
                "Mark Heckler",
                "OReilly Media, Inc.",
                "February 2021",
                "9781492076919",
                "Spring");
        Book three = new Book(3,
                "Hacking with Spring Boot 2.3: Reactive Edition",
                "Greg L. Turnquist",
                "Amazon.com Services LLC",
                "May 2020",
                "B086722L4L",
                "Spring");

        books.addAll(Arrays.asList(one,two,three));
    }
}

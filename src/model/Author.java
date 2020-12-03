package model;

import java.sql.Date;
import java.util.ArrayList;

public class Author {

    private String name;
    private ArrayList<Book> books;

    public Author(String name, ArrayList<Book> books) {

        this.name = name;

        this.books = new ArrayList<>();

    }

    public String getName() {
        return this.name;
    }

    public void addBook(Book b) {
        this.books.add(b);
    }

    @Override
    public String toString() {
        String info = "Name" + this.getName();

        return info;
    }

}

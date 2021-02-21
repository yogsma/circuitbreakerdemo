package com.betterjavacode.books.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Book")
@Table(name = "book")
public class Book implements Serializable
{

    public Book()
    {

    }

    public Book(String title, String author, String isbn)
    {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String isbn;

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getIsbn ()
    {
        return isbn;
    }

    public void setIsbn (String isbn)
    {
        this.isbn = isbn;
    }
}

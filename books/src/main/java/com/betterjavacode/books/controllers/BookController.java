package com.betterjavacode.books.controllers;

import com.betterjavacode.books.daos.BookDao;
import com.betterjavacode.books.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("https://localhost:8443")
@RestController
@RequestMapping("/v1/library")
public class BookController
{
    @Autowired
    BookDao bookDao;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String bookTitle)
    {
        try
        {
            List<Book> listOfBooks = new ArrayList<>();
            if(bookTitle == null || bookTitle.isEmpty())
            {
                bookDao.findAll().forEach(listOfBooks::add);
            }
            else
            {
                bookDao.findByTitleContaining(bookTitle).forEach(listOfBooks::add);
            }

            if(listOfBooks.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(listOfBooks, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long id)
    {
        try
        {
            Optional<Book> bookOptional = bookDao.findById(id);

            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addABookToLibrary(@RequestBody Book book)
    {
        try
        {
            Book createdBook = bookDao.save(new Book(book.getTitle(), book.getAuthor(),
                    book.getIsbn()));
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateABook(@PathVariable("id") long id, @RequestBody Book book)
    {
        Optional<Book> bookOptional = bookDao.findById(id);

        if(bookOptional.isPresent())
        {
            Book updatedBook = bookOptional.get();
            updatedBook.setTitle(book.getTitle());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setIsbn(book.getIsbn());
            return new ResponseEntity<>(bookDao.save(updatedBook), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteABook(@PathVariable("id") long id)
    {
        try
        {
            bookDao.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

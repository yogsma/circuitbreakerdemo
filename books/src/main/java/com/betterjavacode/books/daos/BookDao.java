package com.betterjavacode.books.daos;

import com.betterjavacode.books.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookDao extends JpaRepository<Book, Long>
{
    Set<Book> findByTitleContaining (String bookTitle);
}

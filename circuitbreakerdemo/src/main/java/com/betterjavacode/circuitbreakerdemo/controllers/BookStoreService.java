package com.betterjavacode.circuitbreakerdemo.controllers;

import com.betterjavacode.circuitbreakerdemo.dtos.Book;
import com.betterjavacode.circuitbreakerdemo.managers.BookManager;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;

@Controller
public class BookStoreService
{

    private static final Logger LOGGER = LoggerFactory.getLogger(BookStoreService.class);

    @Autowired
    public BookManager bookManager;

    @Autowired
    private CircuitBreaker timeCircuitBreaker;

    @RequestMapping(value = "/home", method= RequestMethod.GET)
    public String home(HttpServletRequest request, Model model)
    {
        return "home";
    }

    @RequestMapping(value = "/books", method=RequestMethod.GET)
    public String books(HttpServletRequest request, Model model)
    {
        Supplier<List<Book>> booksSupplier =
                timeCircuitBreaker.decorateSupplier(() -> bookManager.getAllBooksFromLibrary());

        LOGGER.info("Going to start calling the REST service with Circuit Breaker");
        List<Book> books = null;
        for(int i = 0; i < 15; i++)
        {
            try
            {
                LOGGER.info("Retrieving books from returned supplier");
                books = booksSupplier.get();
            }
            catch(Exception e)
            {
                LOGGER.error("Could not retrieve books from supplier", e);
            }
        }
        model.addAttribute("books", books);

        return "books";
    }
}

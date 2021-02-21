package com.betterjavacode.circuitbreakerdemo.managers;

import com.betterjavacode.circuitbreakerdemo.dtos.Book;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Component
public class BookManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BookManager.class);

    @Autowired
    private RestTemplate restTemplate;

    public List<Book> getAllBooksFromLibrary ()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<List<Book>> responseEntity;
        long startTime = System.currentTimeMillis();
        LOGGER.info("Start time = {}", startTime);
        try
        {
            responseEntity= restTemplate.exchange(buildUrl(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>()
                    {});
            if(responseEntity != null && responseEntity.hasBody())
            {
                LOGGER.info("Total time to retrieve results = {}",
                        System.currentTimeMillis() - startTime);
                return responseEntity.getBody();
            }
        }
        catch (URISyntaxException e)
        {
            LOGGER.error("URI has a wrong syntax", e);
        }

        LOGGER.info("No result found, returning an empty list");
        return new ArrayList<>();
    }

    private URI buildUrl () throws URISyntaxException
    {
        return new URI("https://localhost:8443/v1/library/books");
    }


}

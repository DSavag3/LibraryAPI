package com.libraryAPI.Library.service;

import com.libraryAPI.Library.exception.BookException;
import com.libraryAPI.Library.model.Book;
import com.libraryAPI.Library.repository.BookRepo;
import com.libraryAPI.Library.repository.GenreRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepo bookRepository;

    @Autowired
    private GenreRepo genreRepository;

    public List<Book> getAllBooks() {
        logger.info("Fetching all books");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        // Log information about the book retrieval
        logger.info("Fetching book with ID: {}", id);

        // Use findById method of bookRepository to get the book by ID
        return bookRepository.findById(id)
                // If the book is present, return it; otherwise, throw an exception
                .orElseThrow(() -> {
                    // Log an error message if the book is not found
                    logger.error("Book with ID: {} not found", id);
                    // Throw a BookException with the book ID
                    return new BookException(id);
                });
    }

    public Book createBook(Book book) {
        logger.info("Creating a new book: {}", book);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        // Validate if the book with the given ID exists before updating
        getBookById(id);

        updatedBook.setId(id);
        logger.info("Updating book with ID: {} to: {}", id, updatedBook);
        return bookRepository.save(updatedBook);
    }

    public void deleteBook(Long id) {
        // Validate if the book with the given ID exists before deleting
        getBookById(id);
        logger.info("Deleting book with ID: {}", id);
        bookRepository.deleteById(id);
    }
    public List<Book> searchBooksByTitle(String title) {
        logger.info("Searching books by title: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
package com.libraryAPI.Library.controller;

import com.libraryAPI.Library.exception.BookException;
import com.libraryAPI.Library.model.Book;
import com.libraryAPI.Library.model.Genre;
import com.libraryAPI.Library.repository.BookRepo;
import com.libraryAPI.Library.service.BookService;
import com.libraryAPI.Library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookRepo bookRepo;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            // Call the bookService to retrieve all books
            List<Book> books = bookService.getAllBooks();
            // Return a ResponseEntity with the list of books and HTTP status OK
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            // If an exception occurs, throw a ResponseStatusException with HTTP status INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching books", e);
        }
    }

    // Get a book by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            // Call the bookService to retrieve a book by its ID
            Book book = bookService.getBookById(id);
            // Return a ResponseEntity with the book and HTTP status OK
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookException e) {
            // If the book is not found, throw a ResponseStatusException with HTTP status NOT_FOUND
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // Create a new book
    @PostMapping
    @Transactional
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            // Check if the book has a new genre
            if (book.getGenre() != null && book.getGenre().getId() == null) {
                // Save the new genre first
                Genre savedGenre = genreService.createGenre(book.getGenre());
                // Set the saved genre in the book
                book.setGenre(savedGenre);
            }

            // Save the book using the book repository
            Book createdBook = bookRepo.save(book);

            // Return a ResponseEntity with the created book and HTTP status CREATED
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // If there is a data integrity violation, throw a ResponseStatusException with HTTP status BAD_REQUEST
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data integrity violation", e);
        } catch (BookException e) {
            // If there is an issue with the book, throw a ResponseStatusException with HTTP status NOT_FOUND
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            // If an unexpected exception occurs, throw a ResponseStatusException with HTTP status INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating book", e);
        }
    }

    // Update an existing book by its ID
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        try {
            // Call the bookService to update the book by its ID
            Book updated = bookService.updateBook(id, updatedBook);
            // If the update is successful, return a ResponseEntity with the updated book and HTTP status OK
            // If the book is not found, return a ResponseEntity with HTTP status NOT_FOUND
            return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BookException e) {
            // If the book is not found, throw a ResponseStatusException with HTTP status NOT_FOUND
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            // If an unexpected exception occurs, throw a ResponseStatusException with HTTP status INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating book", e);
        }
    }

    // Delete a book by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            // Call the bookService to delete the book by its ID
            bookService.deleteBook(id);
            // Return a ResponseEntity with HTTP status NO_CONTENT if the deletion is successful
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BookException e) {
            // If the book is not found, throw a ResponseStatusException with HTTP status NOT_FOUND
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            // If an unexpected exception occurs, throw a ResponseStatusException with HTTP status INTERNAL_SERVER_ERROR
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting book", e);
        }
    }
    @GetMapping("/search")
    public List<Book> searchBooksByTitle(@RequestParam(name = "title") String title) {
        return bookService.searchBooksByTitle(title);
    }
}
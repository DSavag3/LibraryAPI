package com.libraryAPI.Library.controller;

import com.libraryAPI.Library.exception.GenreException;
import com.libraryAPI.Library.model.Genre;
import com.libraryAPI.Library.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {


    @Autowired
    private GenreService genreService;


    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        try {
            // Calling the service to get all genres
            List<Genre> genres = genreService.getAllGenres();
            // Returning a ResponseEntity with the list of genres and HTTP status OK
            return new ResponseEntity<>(genres, HttpStatus.OK);
        } catch (Exception e) {
            // Handling exceptions by throwing a ResponseStatusException with an internal server error status and a custom message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching genres", e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        try {
            // Calling the service to get a genre by ID
            Genre genre = genreService.getGenreById(id);
            // Returning a ResponseEntity with the genre and HTTP status OK
            return new ResponseEntity<>(genre, HttpStatus.OK);
        } catch (GenreException e) {
            // Handling specific exceptions (GenreException) by throwing a ResponseStatusException with a not found status and a custom message
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        try {
            // Calling the service to create a new genre
            Genre createdGenre = genreService.createGenre(genre);
            // Returning a ResponseEntity with the created genre and HTTP status CREATED
            return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handling exceptions by throwing a ResponseStatusException with an internal server error status and a custom message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating genre", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre updatedGenre) {
        try {
            // Calling the service to update an existing genre
            Genre updated = genreService.updateGenre(id, updatedGenre);
            // Returning a ResponseEntity with the updated genre and HTTP status OK, or NOT_FOUND if the genre was not found
            return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Handling exceptions by throwing a ResponseStatusException with an internal server error status and a custom message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating genre", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        try {
            // Calling the service to delete a genre by ID
            genreService.deleteGenre(id);
            // Returning a ResponseEntity with HTTP status NO_CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // Handling exceptions by throwing a ResponseStatusException with an internal server error status and a custom message
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting genre", e);
        }
    }
}
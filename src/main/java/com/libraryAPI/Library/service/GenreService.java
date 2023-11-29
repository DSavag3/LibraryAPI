package com.libraryAPI.Library.service;

import com.libraryAPI.Library.exception.GenreException;
import com.libraryAPI.Library.model.Genre;
import com.libraryAPI.Library.repository.GenreRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private static final Logger logger = LoggerFactory.getLogger(GenreService.class);

    @Autowired
    private GenreRepo genreRepository;

    public List<Genre> getAllGenres() {
        logger.info("Fetching all genres");
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        logger.info("Fetching genre with ID: {}", id);
        return genreRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Genre with ID: {} not found", id);
                    return new GenreException(id);
                });
    }

    public Genre createGenre(Genre genre) {
        // Check if the genre already exists
        Optional<Genre> existingGenre = genreRepository.findByName(genre.getName());
        if (existingGenre.isPresent()) {
            logger.warn("Genre with name '{}' already exists. Returning existing genre.", genre.getName());
            return existingGenre.get();
        } else {
            logger.info("Creating a new genre: {}", genre);
            return genreRepository.save(genre);
        }
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        // Validate if the genre with the given ID exists before updating
        getGenreById(id);

        updatedGenre.setId(id);
        logger.info("Updating genre with ID: {} to: {}", id, updatedGenre);
        return genreRepository.save(updatedGenre);
    }

    public void deleteGenre(Long id) {
        // Validate if the genre with the given ID exists before deleting
        getGenreById(id);
        logger.info("Deleting genre with ID: {}", id);
        genreRepository.deleteById(id);
    }
}

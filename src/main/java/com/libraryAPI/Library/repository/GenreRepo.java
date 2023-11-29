package com.libraryAPI.Library.repository;

import com.libraryAPI.Library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepo extends JpaRepository <Genre, Long> {
    Optional<Genre> findByName(String name);
}

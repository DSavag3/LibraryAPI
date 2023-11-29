package com.libraryAPI.Library.exception;

public class GenreException extends RuntimeException{

    public GenreException(Long id) {
        super("Genre not found with ID: " + id);
    }
}


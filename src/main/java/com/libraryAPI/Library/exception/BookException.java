package com.libraryAPI.Library.exception;

public class BookException extends RuntimeException {
    public BookException(Long id) {
        super("Book not found with ID: " + id);
    }
}


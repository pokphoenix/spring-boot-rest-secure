package com.poktest.spring.boot.rest.secure.api.error;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String id) {
        super("Book id not found : " + id);
    }

}

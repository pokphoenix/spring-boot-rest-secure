package com.poktest.spring.boot.rest.secure.api.repository;

import com.poktest.spring.boot.rest.secure.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}

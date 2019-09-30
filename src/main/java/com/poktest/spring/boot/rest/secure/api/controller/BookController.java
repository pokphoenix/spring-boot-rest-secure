package com.poktest.spring.boot.rest.secure.api.controller;

import com.poktest.spring.boot.rest.secure.api.model.Book;
import com.poktest.spring.boot.rest.secure.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookRepository repo;

    @GetMapping
    List<Book> getAll(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    Book get(@PathVariable("id") Long id){
        return repo.findById(id).
                orElseThrow(()->new EntityNotFoundException());
    }

    @PostMapping
    Book save(@Valid @RequestBody Book data){
        return repo.save(data);
    }

    @PutMapping("/{id}")
    Book saveOrUpdate(@Valid @RequestBody Book data,@PathVariable("id") Long id){
        return repo.findById(id).map(x->{
            x.setName(data.getName());
            x.setAuthor(data.getAuthor());
            x.setPrice(data.getPrice());
            return repo.save(x);
        }).orElseGet(()->{
            data.setId(id);
            return repo.save(data);
        });
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id){
        repo.deleteById(id);
    }

}

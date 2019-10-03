package com.poktest.spring.boot.rest.secure.api.controller;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poktest.spring.boot.rest.secure.api.error.BookNotFoundException;
import com.poktest.spring.boot.rest.secure.api.error.BookUnSupportedFieldPatchException;
import com.poktest.spring.boot.rest.secure.api.model.Book;
import com.poktest.spring.boot.rest.secure.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/book")

public class BookController {

    @Autowired
    BookRepository repo;

    @GetMapping("/all")
    List<Book> getAll(){
        return repo.findAll();
    }

//    @Secured("hasRole('USER')")

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    //    this response content type application/json
    Book get(@PathVariable("id") @Size(min = 3) String id){
        return repo.findById(id).
                orElseThrow(()->new BookNotFoundException(id));
    }

//    @GetMapping("/{id}")
//    ResponseEntity get(@PathVariable("id") String id){
//
////        // method 1 . use header
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
////        return  ResponseEntity.status(200).headers(headers).body(repo.findById(id).
////                orElseThrow(()->new EntityNotFoundException()));
//
//        // method 2 . use content
//        return  ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON_UTF8).body(repo.findById(id).
//                orElseThrow(()->new EntityNotFoundException()));
//
//    }

    @PostMapping
    Book save(@Valid @RequestBody Book data){
        return repo.save(data);
    }

    @PutMapping("/{id}")
    Book saveOrUpdate(@Valid @RequestBody Book data,@PathVariable("id")  String id){
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

    // update author only
    @PatchMapping("/books/{id}")
    Book patch(@RequestBody Map<String, String> update, @PathVariable String id) {

        return repo.findById(id)
                .map(x -> {

                    String author = update.get("author");
                    if (!StringUtils.isEmpty(author)) {
                        x.setAuthor(author);

                        // better create a custom method to update a value = :newValue where id = :id
                        return repo.save(x);
                    } else {
                        throw new BookUnSupportedFieldPatchException(update.keySet());
                    }

                })
                .orElseGet(() -> {
                    throw new BookNotFoundException(id);
                });

    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id){
        repo.deleteById(id);
    }

}

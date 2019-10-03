package com.poktest.spring.boot.rest.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poktest.spring.boot.rest.secure.api.model.Book;
import com.poktest.spring.boot.rest.secure.api.repository.BookRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository mockRepository;

    @BeforeEach
    public void init() {
        Book book = new Book("23232-2f2343-4234234", "Book Name", "Mkyong", new BigDecimal("9.99"));
        when(mockRepository.findById("23232-2f2343-4234234")).thenReturn(Optional.of(book));
    }

    @WithMockUser("USER")
    @Test
    public void find_bookId_OK() throws Exception {

        mockMvc.perform(get("/book/23232-2f2343-4234234"))
                /*.andDo(print())*/
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("23232-2f2343-4234234")))
                .andExpect(jsonPath("$.name", is("Book Name")))
                .andExpect(jsonPath("$.author", is("Mkyong")))
                .andExpect(jsonPath("$.price", is(9.99)));

        verify(mockRepository, times(1)).findById("23232-2f2343-4234234");

    }

    @Test
    public void find_nologin_401() throws Exception {
        mockMvc.perform(get("/book/23232-2f2343-4234234"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser("USER")
    @Test
    public void find_allBook_OK() throws Exception {

        List<Book> books = Arrays.asList(
                new Book("23232-2f2343-4234234", "Book A", "Ah Pig", new BigDecimal("1.99")),
                new Book("KFK20312-213M3123", "Book B", "Ah Dog", new BigDecimal("2.99")));

        when(mockRepository.findAll()).thenReturn(books);

        mockMvc.perform(get("/book"))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("23232-2f2343-4234234")))
                .andExpect(jsonPath("$[0].name", is("Book A")))
                .andExpect(jsonPath("$[0].author", is("Ah Pig")))
                .andExpect(jsonPath("$[0].price", is(1.99)))
                .andExpect(jsonPath("$[1].id", is("KFK20312-213M3123")))
                .andExpect(jsonPath("$[1].name", is("Book B")))
                .andExpect(jsonPath("$[1].author", is("Ah Dog")))
                .andExpect(jsonPath("$[1].price", is(2.99)));

        verify(mockRepository, times(1)).findAll();
    }

    @WithMockUser("USER")
    @Test
    public void find_bookIdNotFound_404() throws Exception {
        mockMvc.perform(get("/book/5"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

//    @WithMockUser(roles="ADMIN")
    @WithMockUser(username = "admin", password = "password", roles = "ADMIN")
    @Test
    public void save_book_OK() throws Exception {

        Book newBook = new Book("pok-test-1", "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
        when(mockRepository.save(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/book")
                .content(om.writeValueAsString(newBook))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                /*.andDo(print())*/
                .andExpect(status().isOk())
//                .andExpect(status().isCreated()) // for 201
                .andExpect(jsonPath("$.id", is("pok-test-1")))
                .andExpect(jsonPath("$.name", is("Spring Boot Guide")))
                .andExpect(jsonPath("$.author", is("mkyong")))
                .andExpect(jsonPath("$.price", is(2.99)));

        verify(mockRepository, times(1)).save(any(Book.class));

    }
}

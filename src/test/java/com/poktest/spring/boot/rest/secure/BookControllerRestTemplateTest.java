//package com.poktest.spring.boot.rest.secure;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.poktest.spring.boot.rest.secure.api.model.Book;
//import com.poktest.spring.boot.rest.secure.api.repository.BookRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@ActiveProfiles("test")
//@SpringBootTest
//public class BookControllerRestTemplateTest {
//    private static final ObjectMapper om = new ObjectMapper();
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @MockBean
//    private BookRepository mockRepository;
//
//    @BeforeEach
//    public void init() {
//        Book book = new Book(1L, "A Guide to the Bodhisattva Way of Life", "Santideva", new BigDecimal("15.41"));
//        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//        System.err.println("book : "+book);
//    }
//
//    @Test
//    public void find_login_ok() throws Exception {
//
//        String expected = "{id:1,name:\"A Guide to the Bodhisattva Way of Life\",author:\"Santideva\",price:15.41}";
//
//        ResponseEntity<String> response = restTemplate
//                .withBasicAuth("user", "password")
//                .getForEntity("/book/1", String.class);
//
//        printJSON(response);
//
//        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        System.err.println(response.getBody());
//        System.err.println("=====");
//        System.err.println(expected);
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//
//    }
//
//    @Test
//    public void find_nologin_401() throws Exception {
//
//        String expected = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Unauthorized\",\"path\":\"/books/1\"}";
//
//        ResponseEntity<String> response = restTemplate
//                .getForEntity("/books/1", String.class);
//
//        printJSON(response);
//
//        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//
//    }
//
//    private static void printJSON(Object object) {
//        String result;
//        try {
//            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//            System.out.println(result);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }
//}

//package com.poktest.spring.boot.rest.secure;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.poktest.spring.boot.rest.secure.api.model.Book;
//import com.poktest.spring.boot.rest.secure.api.repository.BookRepository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.*;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class SecureApplicationTests {
//
//	private static final ObjectMapper om = new ObjectMapper();
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private BookRepository 	mockRepository;
//
//
//	@BeforeEach		//*** Junit5's use @BeforeEach    JUnit 4’s @Before
//	public void init(){
//		Book book = new Book(1L, "A Guide to the Bodhisattva Way of Life", "Santideva", new BigDecimal("15.41"));
//		when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
//	}
//
//	//@WithMockUser(username = "USER")
//	@WithMockUser("USER")
//	@Test
//	public void find_login_ok() throws Exception {
//
//		mockMvc.perform(get("/book/1"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.id", is(1)))
//				.andExpect(jsonPath("$.name", is("A Guide to the Bodhisattva Way of Life")))
//				.andExpect(jsonPath("$.author", is("Santideva")))
//				.andExpect(jsonPath("$.price", is(15.41)));
//
//	}
//
//	@Test
//	public void find_nologin_401() throws Exception {
//		mockMvc.perform(get("/book/1"))
//				.andDo(print())
//				.andExpect(status().isUnauthorized());
//	}
//
//	@Test
//	public void find_bookId_OK() throws Exception {
//
//		mockMvc.perform(get("/book/1"))
//				/*.andDo(print())*/
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.id", is(1)))
//				.andExpect(jsonPath("$.name", is("Book Name")))
//				.andExpect(jsonPath("$.author", is("Mkyong")))
//				.andExpect(jsonPath("$.price", is(9.99)));
//
//		verify(mockRepository, times(1)).findById(1L);
//
//	}
//
//	@Test
//	public void find_allBook_OK() throws Exception {
//
//		List<Book> books = Arrays.asList(
//				new Book(1L, "Book A", "Ah Pig", new BigDecimal("1.99")),
//				new Book(2L, "Book B", "Ah Dog", new BigDecimal("2.99")));
//
//		when(mockRepository.findAll()).thenReturn(books);
//
//		mockMvc.perform(get("/book"))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$", hasSize(2)))
//				.andExpect(jsonPath("$[0].id", is(1)))
//				.andExpect(jsonPath("$[0].name", is("Book A")))
//				.andExpect(jsonPath("$[0].author", is("Ah Pig")))
//				.andExpect(jsonPath("$[0].price", is(1.99)))
//				.andExpect(jsonPath("$[1].id", is(2)))
//				.andExpect(jsonPath("$[1].name", is("Book B")))
//				.andExpect(jsonPath("$[1].author", is("Ah Dog")))
//				.andExpect(jsonPath("$[1].price", is(2.99)));
//
//		verify(mockRepository, times(1)).findAll();
//	}
//
//	@Test
//	public void find_bookIdNotFound_404() throws Exception {
//		mockMvc.perform(get("/book/5")).andExpect(status().isNotFound());
//	}
//
//	@Test
//	public void save_book_OK() throws Exception {
//
//		Book newBook = new Book(1L, "Spring Boot Guide", "mkyong", new BigDecimal("2.99"));
//		when(mockRepository.save(any(Book.class))).thenReturn(newBook);
//
//		mockMvc.perform(post("/books")
//				.content(om.writeValueAsString(newBook))
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				/*.andDo(print())*/
//				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.id", is(1)))
//				.andExpect(jsonPath("$.name", is("Spring Boot Guide")))
//				.andExpect(jsonPath("$.author", is("mkyong")))
//				.andExpect(jsonPath("$.price", is(2.99)));
//
//		verify(mockRepository, times(1)).save(any(Book.class));
//
//	}
//
//	@Test
//	public void update_book_OK() throws Exception {
//
//		Book updateBook = new Book(1L, "ABC", "mkyong", new BigDecimal("19.99"));
//		when(mockRepository.save(any(Book.class))).thenReturn(updateBook);
//
//		mockMvc.perform(put("/books/1")
//				.content(om.writeValueAsString(updateBook))
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.id", is(1)))
//				.andExpect(jsonPath("$.name", is("ABC")))
//				.andExpect(jsonPath("$.author", is("mkyong")))
//				.andExpect(jsonPath("$.price", is(19.99)));
//
//
//	}
//
//	@Test
//	public void patch_bookAuthor_OK() throws Exception {
//
//		when(mockRepository.save(any(Book.class))).thenReturn(new Book());
//		String patchInJson = "{\"author\":\"ultraman\"}";
//
//		mockMvc.perform(patch("/books/1")
//				.content(patchInJson)
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk());
//
//		verify(mockRepository, times(1)).findById(1L);
//		verify(mockRepository, times(1)).save(any(Book.class));
//
//	}
//
//	@Test
//	public void patch_bookPrice_405() throws Exception {
//
//		String patchInJson = "{\"price\":\"99.99\"}";
//
//		mockMvc.perform(patch("/books/1")
//				.content(patchInJson)
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				.andExpect(status().isMethodNotAllowed());
//
//		verify(mockRepository, times(1)).findById(1L);
//		verify(mockRepository, times(0)).save(any(Book.class));
//	}
//
//	@Test
//	public void delete_book_OK() throws Exception {
//
//		doNothing().when(mockRepository).deleteById(1L);
//
//		mockMvc.perform(delete("/books/1"))
//				/*.andDo(print())*/
//				.andExpect(status().isOk());
//
//		verify(mockRepository, times(1)).deleteById(1L);
//	}
//
//	private static void printJSON(Object object) {
//		String result;
//		try {
//			result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//			System.out.println(result);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void save_emptyAuthor_emptyPrice_400() throws Exception {
//
//		String bookInJson = "{\"name\":\"ABC\"}";
//
//		mockMvc.perform(post("/books")
//				.content(bookInJson)
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				.andDo(print())
//				.andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
//				.andExpect(jsonPath("$.status", is(400)))
//				.andExpect(jsonPath("$.errors").isArray())
//				.andExpect(jsonPath("$.errors", hasSize(3)))
//				.andExpect(jsonPath("$.errors", hasItem("Author is not allowed.")))
//				.andExpect(jsonPath("$.errors", hasItem("Please provide a author")))
//				.andExpect(jsonPath("$.errors", hasItem("Please provide a price")));
//
//		verify(mockRepository, times(0)).save(any(Book.class));
//
//	}
//
//	/*
//        {
//            "timestamp":"2019-03-05T09:34:13.207+0000",
//            "status":400,
//            "errors":["Author is not allowed."]
//        }
//     */
//	@Test
//	public void save_invalidAuthor_400() throws Exception {
//
//		String bookInJson = "{\"name\":\" Spring REST tutorials\", \"author\":\"abc\",\"price\":\"9.99\"}";
//
//		mockMvc.perform(post("/books")
//				.content(bookInJson)
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
//				.andDo(print())
//				.andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
//				.andExpect(jsonPath("$.status", is(400)))
//				.andExpect(jsonPath("$.errors").isArray())
//				.andExpect(jsonPath("$.errors", hasSize(1)))
//				.andExpect(jsonPath("$.errors", hasItem("Author is not allowed.")));
//
//		verify(mockRepository, times(0)).save(any(Book.class));
//
//	}
//}

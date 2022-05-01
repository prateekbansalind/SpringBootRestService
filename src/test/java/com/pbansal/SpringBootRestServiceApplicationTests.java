package com.pbansal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbansal.controller.AddResponse;
import com.pbansal.controller.Library;
import com.pbansal.controller.LibraryController;
import com.pbansal.repository.ILibraryRepository;
import com.pbansal.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootRestServiceApplicationTests {

	// Dependencies
	@Autowired
	LibraryController libraryController;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	ILibraryRepository repository;
	@MockBean
	LibraryService libraryService;

	@Test
	void contextLoads() {
	}
	@Test
	public void checkBuildIdTest(){
		LibraryService service = new LibraryService();
		String id = service.buildId("ZAC", 786);
		assertEquals(id, "OLDZAC786");
		String anotherId = service.buildId("MAN", 123);
		assertEquals(anotherId, "MAN123");
	}
	public Library buildLibrary(){
		Library library = new Library();
		library.setBook_name("Learn C++");
		library.setAuthor("Mr. X");
		library.setIsbn("ASD");
		library.setAisle(321);
		library.setId("ASD321");
		return library;
	}
	public Library updateLibrary(){
		Library library = new Library();
		library.setBook_name("Learn Java");
		library.setAuthor("Mosh Corey");
		library.setIsbn("ASD");
		library.setAisle(123);
		library.setId("ASD123");
		return library;
	}
	@Test
	public void addBookTest(){
		Library library = buildLibrary();
		// mock to get id.
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		// mock to declare that record belongs to the provided id is not present in the database.
		when(libraryService.checkBookAlreadyExist(library.getId())).thenReturn(false);
		// mock ILibraryRepository
		when(repository.save(any())).thenReturn(library);
		// Assert HTTP response
		ResponseEntity response = libraryController.addBookImplementation(buildLibrary());
		System.out.println(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		// Assert response
		AddResponse addResponse = (AddResponse) response.getBody();
		assertEquals(addResponse.getId(), library.getId());
		assertEquals("Success book is added.", addResponse.getMessage());
	}
	@Test
	// This is serverless mockMvc Test
	public void addBookControllerTest() throws Exception {
		Library library = buildLibrary();;
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(library);
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		when(libraryService.checkBookAlreadyExist(library.getId())).thenReturn(false);
		when(repository.save(any())).thenReturn(library);
		this.mockMvc
				.perform(post("/addBook").contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(library.getId()));
	}

	// unit test for get http method to call records based on author name.
	@Test
	public void getBookBasedOnAuthorNameControllerTest() throws Exception {
		List<Library> li = new ArrayList<Library>();
		li.add(buildLibrary());
		li.add(buildLibrary());
		when(repository.findAllByAuthor(any())).thenReturn(li);
		this.mockMvc
				.perform(get("/getBooks/author").param("authorName", "Mr. X"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].id").value("ASD321"));
	}

	// unit test to authenticate update book controller.
	@Test
	public void authenticateUpdateBookControllerTest() throws Exception {
		Library record = buildLibrary();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(updateLibrary());
		when(libraryService.getBookById(any())).thenReturn(record);
		String responseBody = "{\"book_name\":\"Learn Java\",\"isbn\":\"ASD\",\"aisle\":123,\"id\":\"ASD321\",\"author\":\"Mosh Corey\"}";
		this.mockMvc
				.perform(put("/updateBook/"+record.getId())
						.contentType((MediaType.APPLICATION_JSON))
						.content(jsonString))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(responseBody));
	}

	 // unit test to authenticate delete book controller.
	@Test
	public void deleteBookControllerTest() throws Exception {
		Library record = buildLibrary();
		when(libraryService.getBookById(any())).thenReturn(record);
		when(libraryService.checkBookAlreadyExist(any())).thenReturn(true);
		doNothing().when(repository).delete(record);
		this.mockMvc
				.perform(delete("/deleteBook/"+record.getId()))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string("Book is deleted"));
	}
}
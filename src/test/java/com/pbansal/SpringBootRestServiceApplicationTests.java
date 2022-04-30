package com.pbansal;
import com.pbansal.controller.AddResponse;
import com.pbansal.controller.Library;
import com.pbansal.controller.LibraryController;
import com.pbansal.repository.ILibraryRepository;
import com.pbansal.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringBootRestServiceApplicationTests {

	// Dependencies
	@Autowired
	LibraryController libraryController;

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

	@Test
	public void addBookTest(){
		Library library = new Library();
		// mock to get id.
		when(libraryService.buildId(library.getIsbn(), library.getAisle())).thenReturn(library.getId());
		// mock to declare that record belongs to the provided id is not present in the database.
		when(libraryService.checkBookAlreadyExist(library.getId())).thenReturn(false);
		// Assert HTTP response
		ResponseEntity response = libraryController.addBookImplementation(buildLibrary());
		System.out.println(response.getStatusCode());
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		// Assert response
		AddResponse addResponse = (AddResponse) response.getBody();
		assertEquals(addResponse.getId(), library.getId());
		assertEquals("Success book is added.", addResponse.getMessage());
	}

	public AddResponse buildResponseBody(){
		AddResponse addResponse = new AddResponse();
		addResponse.setMessage("Success book is added.");
		addResponse.setId("ASD321");
		return addResponse;
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
}
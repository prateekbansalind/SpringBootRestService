package com.pbansal;

import com.pbansal.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class SpringBootRestServiceApplicationTests {

	// Dependencies
	@Autowired
	LibraryService service;
	@Test
	void contextLoads() {
	}
	@Test
	public void checkBuildIdLogic(){
		String id = service.buildId("ZAC", 786);
		assertEquals(id, "OLDZAC786");
		String anotherId = service.buildId("MAN", 123);
		assertEquals(anotherId, "MAN123");
	}

}

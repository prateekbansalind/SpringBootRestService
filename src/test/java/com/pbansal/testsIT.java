package com.pbansal;

import com.pbansal.controller.Library;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest
public class testsIT {

    public Library buildLibrary(){
        Library library = new Library();
        library.setBook_name("Learn C++");
        library.setAuthor("Mr. X");
        library.setIsbn("XSD");
        library.setAisle(5521);
        library.setId("XSD5521");
        return library;
    }

    @Test
    public void getAuthorNameBooksTest() throws JSONException {
        String expected = "[{\"book_name\":\"Karate API\",\"isbn\":\"abcd\",\"aisle\":78,\"id\":\"abcd78\",\"author\":\"Xavier\"}]";
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String > response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?authorName=Xavier", String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    public void addBookIntegrationTest(){
        TestRestTemplate restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Library> request = new HttpEntity<Library>(buildLibrary(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        System.out.println(response.getHeaders().get("unique").get(0));
        Assert.assertEquals(buildLibrary().getId(), response.getHeaders().get("unique").get(0));
    }

}

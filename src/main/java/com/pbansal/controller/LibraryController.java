package com.pbansal.controller;

import com.pbansal.repository.ILibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {
    @Autowired
    ILibraryRepository repository;


    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library){
        library.setId(library.getIsbn()+library.getAisle());
        String id = library.getId();
        repository.save(library);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Unique", id);

        AddResponse addResponse = new AddResponse();
        addResponse.setMessage("Success book is added.");
        addResponse.setId(id);

        return new ResponseEntity<AddResponse>(addResponse,httpHeaders,HttpStatus.CREATED);
    }
}

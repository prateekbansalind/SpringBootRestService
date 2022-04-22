package com.pbansal.controller;

import com.pbansal.repository.ILibraryRepository;
import com.pbansal.service.LibraryService;
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

    @Autowired
    LibraryService libraryService;


    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library){

        String id = libraryService.buildId(library.getIsbn(), library.getAisle());

        AddResponse addResponse = new AddResponse();

        if (!libraryService.checkBookAlreadyExist(id)) {
            library.setId(id);
            repository.save(library);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Unique", id);


            addResponse.setMessage("Success book is added.");
            addResponse.setId(id);

            return new ResponseEntity<AddResponse>(addResponse,httpHeaders,HttpStatus.CREATED);
        }
        else{
            addResponse.setMessage("Book is already exist.");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse,HttpStatus.ACCEPTED);
        }

    }
}

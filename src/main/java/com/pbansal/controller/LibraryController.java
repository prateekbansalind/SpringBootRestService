package com.pbansal.controller;

import com.pbansal.repository.ILibraryRepository;
import com.pbansal.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    ILibraryRepository repository;
    @Autowired
    LibraryService libraryService;
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library){
        String id = libraryService.buildId(library.getIsbn(), library.getAisle());
        AddResponse addResponse = new AddResponse();
        if (!libraryService.checkBookAlreadyExist(id)) {
            logger.info("Book doesn't exist in the database, so creating one.");
            library.setId(id);
            repository.save(library);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Unique", id);
            addResponse.setMessage("Success book is added.");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse,httpHeaders,HttpStatus.CREATED);
        }
        else{
            logger.info("Book is already exist. So, skipping the creation operation.");
            addResponse.setMessage("Book is already exist.");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping(value = "/getBook/{id}")
    public Library getBookByIdImplementation(@PathVariable(value = "id") String id){
        try {
            logger.info("Retrieve book record by its id.");
            Library lib = repository.findById(id).get();
            return lib;
        }
        catch (Exception e){
            logger.info("Book is not present with this id.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBooks/author")
    public List<Library> getBookByAuthorNameImpl(@RequestParam(value = "authorName") String authorName){
        logger.info("Retrieve all the books with this author name.");
        return repository.findAllByAuthor(authorName);
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Library> updateBookByIdImpl(@PathVariable(value = "id") String id, @RequestBody Library library){
        logger.info("Update new information to the queried ID.");
        Library existingRecord = repository.findById(id).get();
        existingRecord.setAisle(library.getAisle());
        existingRecord.setAuthor(library.getAuthor());
        existingRecord.setBook_name(library.getBook_name());
        repository.save(existingRecord);
        return new ResponseEntity<Library>(existingRecord, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBookByIdImpl(@PathVariable(value = "id") String id) {
        Library record = repository.getById(id);
        if (libraryService.checkBookAlreadyExist(id)) {
            logger.info("Delete records based on the id provided.");
            repository.delete(record);
            return new ResponseEntity<String>("Book is deleted", HttpStatus.CREATED);
        } else {
            logger.info("Book cannot be deleted as the book is not present.");
            return new ResponseEntity<String>("Book is not present", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBooks")
    public List<Library> getAllBooksImpl(){
        logger.info("Retrieve all the records in the database.");
        List<Library> allRecords = repository.findAll();
        return allRecords;
    }
}

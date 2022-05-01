package com.pbansal.service;

import com.pbansal.controller.Library;
import com.pbansal.repository.ILibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    ILibraryRepository repository;

    public String buildId(String isbn, int aisle){
        if (isbn.startsWith("Z")){
            return "OLD" + isbn + aisle;
        }
        return isbn + aisle;
    }

    public boolean checkBookAlreadyExist(String id){
        Optional<Library> library = repository.findById(id);
        if (library.isPresent())
            return true;
        else
            return false;
    }

    public Library getBookById(String id){
        return repository.findById(id).get();
    }


}

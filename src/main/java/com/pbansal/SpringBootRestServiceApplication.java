
package com.pbansal;

import com.pbansal.controller.Library;
import com.pbansal.repository.ILibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpringBootRestServiceApplication implements CommandLineRunner {

	@Autowired
	ILibraryRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestServiceApplication.class, args);
	}

	@Override
	public void run(String[] args){
		// Retrieve author name based on Id "fdsefr343".
		Library lib1 = repository.findById("fdsefr343").get();
		System.out.println(lib1.getAuthor());

		// add another record in the sql database
		Library lib2 = new Library();
		lib2.setAisle(123);
		lib2.setId("XYZ123");
		lib2.setAuthor("Professor X");
		lib2.setBook_name("Learn Mutant History");
		lib2.setIsbn("XYZ");
		repository.save(lib2);
		List<Library> allRecords = repository.findAll();
		for (Library entity : allRecords){
			System.out.println(entity.getAuthor());
		}

		repository.delete(lib2);

	}


}
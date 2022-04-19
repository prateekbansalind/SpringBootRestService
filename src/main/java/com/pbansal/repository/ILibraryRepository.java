package com.pbansal.repository;

import com.pbansal.controller.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILibraryRepository extends JpaRepository<Library, String> {

}

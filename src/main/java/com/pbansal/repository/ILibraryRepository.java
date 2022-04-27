package com.pbansal.repository;

import com.pbansal.controller.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILibraryRepository extends JpaRepository<Library, String>, ILibraryRepositoryCustom {

}

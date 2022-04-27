package com.pbansal.repository;

import com.pbansal.controller.Library;

import java.util.List;

public interface ILibraryRepositoryCustom {
    List<Library> findAllByAuthor(String authorName);
}

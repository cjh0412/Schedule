package com.example.todo.author.repository;

import com.example.todo.author.dto.AuthorResponseDto;
import com.example.todo.author.entity.Author;

import java.util.Optional;

public interface AuthorRepository {

    Optional<Author> findByAuthorId(String name, String email);
    AuthorResponseDto createAuthor(String name, String email);
    int updateAuthor( Long id, String name);
}

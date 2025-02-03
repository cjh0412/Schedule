package com.example.todo.author.service;


import com.example.todo.author.dto.AuthorResponseDto;
import com.example.todo.author.entity.Author;

import java.util.Optional;

public interface AuthorService {
    Optional<Author> findByAuthorId(String name, String email);
    AuthorResponseDto createAuthor(String name, String email);
    int updateAuthor(Long id , String name);
}

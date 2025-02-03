package com.example.todo.author.service;

import com.example.todo.author.dto.AuthorResponseDto;
import com.example.todo.author.entity.Author;
import com.example.todo.author.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Optional<Author> findByAuthorId(String name, String email) {
        return authorRepository.findByAuthorId(name, email);
    }

    @Override
    public AuthorResponseDto createAuthor(String name, String email) {
        return authorRepository.createAuthor(name, email);
    }

    @Override
    public int updateAuthor(Long id, String name) {
        return authorRepository.updateAuthor(id, name);
    }
}

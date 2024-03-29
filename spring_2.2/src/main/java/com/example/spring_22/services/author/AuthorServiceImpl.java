package com.example.spring_22.services.author;

import com.example.spring_22.dao.author.AuthorRepository;
import com.example.spring_22.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Author saveAuthorIfNotExists(Author author) {
        return authorRepository.getByFullName(author.getFullName()).orElse(authorRepository.save(author));
    }
}

package com.example.spring_22.dao.book;

import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Genre;

import java.util.Optional;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(long id);

    List<Book> findAll();

    void updateNameById(long id, String newName);

    void deleteById(long id);

    void addGenreToBook(long bookId, Genre genre);

    void updateAuthor(long id, Author author);

    void deleteBookGenreLinks(long bookId);

    void deleteGenreFromBook(long bookId, Genre genre);
}

package com.example.spring_22.dao.book;

import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository{

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            entityManager.persist(book);
            return book;
        } else {
            return entityManager.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public void updateNameById(long id, String newName) {
        Query query = entityManager.createQuery("update Book b " +
                "set b.name = :name " +
                "where b.id = :id");
        query.setParameter("name", newName);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery("delete " +
                "from Book b " +
                "where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void addGenreToBook(long bookId, Genre genre) {
        Optional<Book> bookOptional = findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no book with such id");
        }
        Book book = bookOptional.get();
        book.getGenres().add(genre);
        entityManager.merge(book);
    }

    @Override
    public void updateAuthor(long bookId, Author author) {
        Optional<Book> bookOptional = findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no book with such id");
        }
        Book book = bookOptional.get();
        book.setAuthor(author);
        entityManager.merge(book);
    }

    @Override
    public void deleteBookGenreLinks(long bookId) {
        Optional<Book> bookOptional = findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no book with such id");
        }
        Book book = bookOptional.get();
        book.setGenres(new ArrayList<>());
        entityManager.merge(book);
    }

    @Override
    public void deleteGenreFromBook(long bookId, Genre genre) {
        Optional<Book> bookOptional = findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("There is no book with such id");
        }
        Book book = bookOptional.get();
        book.getGenres().remove(genre);
        entityManager.merge(book);
    }
}

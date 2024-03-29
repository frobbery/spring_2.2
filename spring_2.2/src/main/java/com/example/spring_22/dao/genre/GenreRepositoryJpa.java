package com.example.spring_22.dao.genre;

import com.example.spring_22.domain.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryJpa implements GenreRepository{

    private final EntityManager entityManager;

    @Override
    public Optional<Genre> getByName(String genreName) {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g " +
                "where g.genreName = :genre_name", Genre.class);
        query.setParameter("genre_name", genreName);
        List<Genre> genres = query.getResultList();
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0));
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0) {
            entityManager.persist(genre);
            return genre;
        } else {
            return entityManager.merge(genre);
        }
    }
}

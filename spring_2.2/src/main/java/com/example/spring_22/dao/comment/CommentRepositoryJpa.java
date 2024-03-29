package com.example.spring_22.dao.comment;

import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {

    private final EntityManager entityManager;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() <= 0) {
            entityManager.persist(comment);
            return comment;
        } else {
            return entityManager.merge(comment);
        }
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c " +
                "where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public void updateTextById(long id, String newText) {
        Query query = entityManager.createQuery("update Comment c " +
                "set c.text = :text " +
                "where c.id = :id");
        query.setParameter("text", newText);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Query query = entityManager.createQuery("delete " +
                "from Comment c " +
                "where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void updateCommentBook(Comment savedComment, Book book) {
        savedComment.setBook(book);
        entityManager.merge(savedComment);
    }
}

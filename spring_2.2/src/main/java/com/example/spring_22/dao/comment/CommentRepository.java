package com.example.spring_22.dao.comment;

import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long bookId);

    void updateTextById(long id, String newText);

    void deleteById(long id);

    void updateCommentBook(Comment savedComment, Book book);
}

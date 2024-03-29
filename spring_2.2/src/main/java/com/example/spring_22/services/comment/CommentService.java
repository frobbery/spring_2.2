package com.example.spring_22.services.comment;

import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    long addCommentToBook(Comment comment);

    Optional<Comment> getCommentById(long id);

    List<Comment> getAllCommentsOfBook(long bookId);

    void updateCommentTextById(long id, String newText);

    void deleteCommentById(long id);
}

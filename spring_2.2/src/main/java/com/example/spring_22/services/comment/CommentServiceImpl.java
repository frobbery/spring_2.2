package com.example.spring_22.services.comment;

import com.example.spring_22.dao.comment.CommentRepository;
import com.example.spring_22.domain.Comment;
import com.example.spring_22.services.book.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookServiceImpl bookService;

    @Override
    @Transactional
    public long addCommentToBook(Comment comment) {
        var savedComment = commentRepository.save(Comment.builder().text(comment.getText()).build());
        var book = bookService.getBookById(comment.getBook().getId());
        book.ifPresent(value -> commentRepository.updateCommentBook(savedComment, value));
        return savedComment.getId();
    }

    @Override
    public Optional<Comment> getCommentById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> getAllCommentsOfBook(long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public void updateCommentTextById(long id, String newText) {
        commentRepository.updateTextById(id, newText);
    }

    @Override
    @Transactional
    public void deleteCommentById(long id) {
        commentRepository.deleteById(id);
    }
}

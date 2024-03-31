package com.example.spring_22.services.comment;

import com.example.spring_22.dao.comment.CommentRepository;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;
import com.example.spring_22.services.book.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с комментариями должен:")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CommentServiceImpl sut;

    @Test
    @DisplayName("Должен добавлять комментарий к книге")
    void shouldAddCommentToBook() {
        //given
        var commentToBeSaved = Comment.builder()
                .text("text")
                .build();
        var commentFromDao = Comment.builder()
                .id(2L)
                .text("text")
                .build();
        var book = Book.builder()
                .id(1L)
                .build();
        var comment = Comment.builder()
                .text("text")
                .book(book)
                .build();
        when(commentRepository.save(commentToBeSaved)).thenReturn(commentFromDao);
        when(bookService.getBookById(book.getId())).thenReturn(Optional.of(book));

        //when
        var result = sut.addCommentToBook(comment);

        //then
        assertEquals(commentFromDao.getId(), result);
        verify(commentRepository, times(1)).updateCommentBook(commentFromDao, book);
    }

    @Test
    @DisplayName("Должен получать комментарий по id")
    void shouldGetCommentById() {
        //given
        var expectedComment = mock(Comment.class);
        when(commentRepository.findById(expectedComment.getId())).thenReturn(Optional.of(expectedComment));

        //when
        var actualComment = sut.getCommentById(expectedComment.getId());

        //then
        assertThat(actualComment)
                .isPresent()
                .contains(expectedComment);
    }

    @Test
    @DisplayName("Должен получать все комментарии по книге")
    void shouldGetAllCommentsOfBook() {
        //given
        var bookId = 1L;
        var expectedComments = List.of(mock(Comment.class));
        when(commentRepository.findAllByBookId(bookId)).thenReturn(expectedComments);

        //when
        var actualComments = sut.getAllCommentsOfBook(bookId);

        //then
        assertThat(actualComments)
                .containsAll(expectedComments);
    }

    @Test
    @DisplayName("Должен обновлять текст комментария")
    void shouldUpdateCommentTextById() {
        //given
        var comment = mock(Comment.class);

        //when
        sut.updateCommentTextById(comment.getId(), comment.getText());

        //then
        verify(commentRepository, times(1)).updateTextById(comment.getId(), comment.getText());
    }

    @Test
    @DisplayName("Должен удалять комментарий по id")
    void deleteCommentById() {
        //given
        var comment = mock(Comment.class);

        //when
        sut.deleteCommentById(comment.getId());

        //then
        verify(commentRepository, times(1)).deleteById(comment.getId());
    }
}